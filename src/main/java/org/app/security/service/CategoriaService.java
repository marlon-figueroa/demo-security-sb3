package org.app.security.service;

import org.app.security.datatables.Datatables;
import org.app.security.datatables.DatatablesColumnas;
import org.app.security.domain.Categoria;
import org.app.security.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository repository;

    @Autowired
	private Datatables datatables;

    @Transactional(readOnly = true)
    public Categoria buscarPorTitulo(String titulo) {
        return repository.findByTitulo(titulo).orElse(new Categoria());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findCategorias(HttpServletRequest request) {
        datatables.setRequest(request);
		datatables.setColunas(DatatablesColumnas.CATEGORIAS);
		Page<?> page = datatables.getSearch().isEmpty()
				? repository.findAll(datatables.getPageable())
				: repository.findAllByTitulo(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
    }

    @Transactional(readOnly = false)
    public void salvar(Categoria categoria) {
        repository.save(categoria);
    }

    @Transactional(readOnly = false)
    public void editar(Categoria categoria) {
        Categoria p2 = repository.findById(categoria.getId()).get();
        p2.setTitulo(categoria.getTitulo());
        p2.setSubcategoria(categoria.getSubcategoria());
    }

}
