package org.app.security.seeder;

import java.util.ArrayList;
import java.util.List;

import org.app.security.domain.Perfil;
import org.app.security.domain.PerfilTipo;
import org.app.security.domain.Usuario;
import org.app.security.repository.PerfilRepository;
import org.app.security.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {

	private final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
	private final UsuarioRepository userRepository;
	private final JdbcTemplate jdbcTemplate;
	private final PerfilRepository perfilRepository;

	public DatabaseSeeder(UsuarioRepository userRepository, PerfilRepository perfilRepository,
			JdbcTemplate jdbcTemplate) {
		this.userRepository = userRepository;
		this.perfilRepository = perfilRepository;
		this.jdbcTemplate = jdbcTemplate;
	}

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		seedRolesTable();
		seedUsersTable();
	}

	private void seedRolesTable() {
		String sql = "SELECT id, descricao FROM perfil r";
		List<Perfil> r = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
		if (r == null || r.size() <= 0) {
			Perfil roleAdmin = new Perfil();
			roleAdmin.setDesc(PerfilTipo.ADMIN.getDesc());
			perfilRepository.save(roleAdmin);
			logger.info("Role Admin Seeded");

			Perfil roleUser = new Perfil();
			roleUser.setDesc(PerfilTipo.MEDICO.getDesc());
			perfilRepository.save(roleUser);
			logger.info("Role User Seeded");

			Perfil roleModerator = new Perfil();
			roleModerator.setDesc(PerfilTipo.PACIENTE.getDesc());
			perfilRepository.save(roleModerator);
			logger.info("Role Moderator Seeded");
		} else {
			logger.trace("Roles Seeding Not Required");
		}
	}

	/**
	 * Metodo encargado de crear usuario default con roles
	 *
	 * @author mfigueroa
	 */
	private void seedUsersTable() {
		String sql = "SELECT email FROM usuarios u WHERE u.email = \"marlon.f.1993@gmail.com\" LIMIT 1";
		List<Usuario> u = jdbcTemplate.query(sql, (resultSet, rowNum) -> null);
		if (u == null || u.size() <= 0) {
			Usuario user = new Usuario();
			user.setEmail("marlon.f.1993@gmail.com");
			user.setSenha(new BCryptPasswordEncoder().encode("orion1993"));
			user.setActivo(true);
			List<Perfil> roles = new ArrayList<>();
			String argString = "Error: Role is not found.";
			Perfil adminRole = perfilRepository.findByDesc(PerfilTipo.ADMIN.getDesc())
					.orElseThrow(() -> new RuntimeException(argString));
			Perfil userRole = perfilRepository.findByDesc(PerfilTipo.MEDICO.getDesc())
					.orElseThrow(() -> new RuntimeException(argString));
			Perfil pacienteRole = perfilRepository.findByDesc(PerfilTipo.PACIENTE.getDesc())
					.orElseThrow(() -> new RuntimeException(argString));
			roles.add(adminRole);
			roles.add(userRole);
			roles.add(pacienteRole);
			user.setPerfil(roles);
			userRepository.save(user);
			logger.info("Users Seeded");
		} else {
			logger.trace("Users Seeding Not Required");
		}
	}

}
