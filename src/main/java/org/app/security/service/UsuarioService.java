package org.app.security.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.app.security.datatables.Datatables;
import org.app.security.datatables.DatatablesColumnas;
import org.app.security.domain.Perfil;
import org.app.security.domain.PerfilTipo;
import org.app.security.domain.Usuario;
import org.app.security.exception.AccesoNegadoException;
import org.app.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private Datatables datatables;
	
	@Autowired
	private EmailService emailService;

	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {

		return repository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmailEAtivo(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario " + username + " no encontrado."));
		return new User(usuario.getEmail(), usuario.getSenha(),
				AuthorityUtils.createAuthorityList(getAtuthorities(usuario.getPerfil())));
	}

	private String[] getAtuthorities(List<Perfil> perfil) {
		String[] authorities = new String[perfil.size()];
		for (int i = 0; i < perfil.size(); i++) {
			authorities[i] = perfil.get(i).getDesc();
		}
		return authorities;
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColumnas.USUARIOS);
		Page<Usuario> page = datatables.getSearch().isEmpty() ? 
				repository.findAll(datatables.getPageable()) : 
				repository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		repository.save(usuario);
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorIdEPerfil(Long usuarioId, Long[] perfilId) {
		return repository.findByIdAndPerfil(usuarioId, perfilId).orElseThrow(() -> new UsernameNotFoundException("Usuario inexistente!"));
	}

	public static boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaArmazenada);
	}

	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		repository.save(usuario);
	}

	@Transactional(readOnly = false)
	public void salvarCadastroPaciente(Usuario usuario) throws MessagingException {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuario.addPerfil(PerfilTipo.PACIENTE);
		repository.save(usuario);
		emailDeConfirmacaoDeCadastro(usuario.getEmail());
	}

	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmailEAtivo(String email) {
		return repository.findByEmailAndActivo(email);
	}

	public void emailDeConfirmacaoDeCadastro(String email) throws MessagingException {
		String codigo = java.util.Base64.getEncoder().encodeToString(email.getBytes());
		emailService.enviarPedidoDeConfirmacaoDeCadastro(email, codigo);
	}

	@Transactional(readOnly = false)
	public void ativarCadastroPaciente(String codigo) {
		String email = new String(java.util.Base64.getDecoder().decode(codigo));
		Usuario usuario = buscarPorEmail(email);
		if (usuario.hasNotId()) {
			throw new AccesoNegadoException("No fue posible activar si registro. Ingrese en " + "contacto con soporte.");
		}
		usuario.setActivo(true);
	}

	@Transactional(readOnly = false)
	public void pedidoRedefinicaoDeSenha(String email) throws MessagingException {
		Usuario usuario = buscarPorEmailEAtivo(email).orElseThrow(() -> new UsernameNotFoundException("Usuario " + email + " no encontrado."));
		String verificador = RandomStringUtils.randomAlphanumeric(6);
		usuario.setCodigoVerificador(verificador);
		emailService.enviarPedidoRedefinicaoSenha(email, verificador);
	}
}
