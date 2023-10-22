package org.app.security.web.controller;

import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	// abrir pagina home
	@GetMapping({ "/", "/home" })
	public String home(HttpServletResponse response) {
		return "home";
	}

	// abrir pagina login
	@GetMapping({ "/login" })
	public String login() {

		return "login";
	}

	// login invalido
	@GetMapping({ "/login-error" })
	public String loginError(ModelMap model, HttpServletRequest resp) {
		HttpSession session = resp.getSession();
		String lastException = String.valueOf(session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION"));
		if (lastException.contains(SessionAuthenticationException.class.getName())) {
			model.addAttribute("alerta", "error");
			model.addAttribute("titulo", "Acceso rechazado!");
			model.addAttribute("texto", "Ya has iniciado sesión en otro dispositivo.");
			model.addAttribute("subtexto", "Cierra sesión o espera a que caduque tu sesión.");
			return "login";
		}
		model.addAttribute("alerta", "error");
		model.addAttribute("titulo", "Crendenciales inválidas!");
		model.addAttribute("texto", "Nombre de usuario o contraseña incorrectos, inténtelo de nuevo.");
		model.addAttribute("subtexto", "Acceso permitido sólo para registros ya activados.");
		return "login";
	}

	@GetMapping("/expired")
	public String sessaoExpirada(ModelMap model) {
		model.addAttribute("alerta", "error");
		model.addAttribute("titulo", "Acceso rechazado!");
		model.addAttribute("texto", "Su sesión ha caducado.");
		model.addAttribute("subtexto", "Iniciaste sesión en otro dispositivo");
		return "login";
	}

	// acesso negado
	@GetMapping({ "/acesso-negado" })
	public String acessoNegado(ModelMap model, HttpServletResponse resp) {
		model.addAttribute("status", resp.getStatus());
		model.addAttribute("error", "Acceso denegado");
		model.addAttribute("message", "No tienes permiso para acceder a esta área o acción.");
		return "error";
	}
}
