package org.app.security.web.controller;

import java.time.LocalDate;

import org.app.security.domain.Agendamento;
import org.app.security.domain.Especialidade;
import org.app.security.domain.Paciente;
import org.app.security.domain.PerfilTipo;
import org.app.security.service.AgendamentoService;
import org.app.security.service.EspecialidadeService;
import org.app.security.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {

	@Autowired
	private AgendamentoService service;
	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private EspecialidadeService especialidadeService;

	// abre a pagina de programacion de consultas
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping({ "/agendar" })
	public String agendarConsulta(Agendamento agendamento) {

		return "agendamento/cadastro";
	}

	// busca los horarios libres, ou seja, sem programacion
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping("/horario/medico/{id}/data/{data}")
	public ResponseEntity<?> getHorarios(@PathVariable("id") Long id,
			@PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {

		return ResponseEntity.ok(service.buscarHorariosNaoAgendadosPorMedicoIdEData(id, data));
	}

	// salvar um consulta agendada
	@PreAuthorize("hasAuthority('PACIENTE')")
	@PostMapping({ "/salvar" })
	public String salvar(@AuthenticationPrincipal User user, RedirectAttributes attr, Agendamento agendamento) {
		Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
		String titulo = agendamento.getEspecialidad().getTitulo();
		Especialidade especialidade = especialidadeService.buscarPorTitulos(new String[] { titulo }).stream()
				.findFirst().get();
		agendamento.setEspecialidad(especialidade);
		agendamento.setPaciente(paciente);
		service.salvar(agendamento);
		attr.addFlashAttribute("sucesso", "Su consulta fue agendada con exito.");
		return "redirect:/agendamentos/agendar";
	}

	// abrir pagina de historico de agendamento do paciente
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping({ "/historico/paciente", "/historico/consultas" })
	public String historico() {

		return "agendamento/historico-paciente";
	}

	// localizar o historico de agendamentos por usuario logado
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping("/datatables/server/historico")
	public ResponseEntity<?> historicoAgendamentosPorPaciente(HttpServletRequest request,
			@AuthenticationPrincipal User user) {

		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc()))) {

			return ResponseEntity.ok(service.buscarHistoricoPorPacienteEmail(user.getUsername(), request));
		}

		if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc()))) {

			return ResponseEntity.ok(service.buscarHistoricoPorMedicoEmail(user.getUsername(), request));
		}

		return ResponseEntity.notFound().build();
	}

	// localizar programacion pelo id e envia-lo para a pagina de cadastro
	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@GetMapping("/editar/consulta/{id}")
	public String preEditarConsultaPaciente(@PathVariable("id") Long id, ModelMap model,
			@AuthenticationPrincipal User user) {

		Agendamento agendamento = service.buscarPorIdEUsuario(id, user.getUsername());

		model.addAttribute("agendamento", agendamento);
		return "agendamento/cadastro";
	}

	@PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
	@PostMapping("/editar")
	public String editarConsulta(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) {
		String titulo = agendamento.getEspecialidad().getTitulo();
		Especialidade especialidade = especialidadeService.buscarPorTitulos(new String[] { titulo }).stream()
				.findFirst().get();
		agendamento.setEspecialidad(especialidade);

		service.editar(agendamento, user.getUsername());
		attr.addFlashAttribute("sucesso", "Su consulta fue alterada con exito.");
		return "redirect:/agendamentos/agendar";
	}

	@PreAuthorize("hasAuthority('PACIENTE')")
	@GetMapping("/excluir/consulta/{id}")
	public String excluirConsulta(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Consulta excluída con exito.");
		return "redirect:/agendamentos/historico/paciente";
	}

}
