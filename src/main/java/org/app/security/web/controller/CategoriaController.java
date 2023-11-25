package org.app.security.web.controller;

import org.app.security.domain.Categoria;
import org.app.security.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    // abrir pagina de datos de la categoris de productos
    @GetMapping({"/parent-category-data"})
    public String abrirDatosCategoriaPadre(ModelMap model, @AuthenticationPrincipal User user) {
        List<Categoria> parents = new ArrayList();
        parents = service.findParents(user.getUsername());
        model.addAttribute("principales", parents);
        return "categoria/registro";
    }

}
