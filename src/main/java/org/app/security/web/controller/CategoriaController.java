package org.app.security.web.controller;

import org.app.security.domain.Categoria;
import org.app.security.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    // abrir pagina de datos de la categoris de productos
    @GetMapping({ "/lista" })
    public String abrirDatosCategoriaPadre(ModelMap model, @AuthenticationPrincipal User user) {
        model.addAttribute("categoria", new Categoria());
        return "categoria/registro";
    }

    @GetMapping("/datatables/server")
    public ResponseEntity<?> getEspecialidades(HttpServletRequest request) {
        return ResponseEntity.ok(service.findCategorias(request));
    }

    @PostMapping("/salvar")
	public String salvar(Categoria entity, RedirectAttributes attr) {
		service.salvar(entity);
		attr.addFlashAttribute("sucesso", "Operación realizada con éxito!");
		return "redirect:/categorias/lista";
	}

}
