package org.app.security.service;

import org.app.security.domain.Categoria;
import org.app.security.domain.Paciente;
import org.app.security.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository repository;

    @Transactional(readOnly = true)
    public Categoria buscarPorTitulo(String titulo) {
        return repository.findByTitulo(titulo).orElse(new Categoria());
    }

    @Transactional(readOnly = true)
    public List<Categoria> findParents(String titulo) {
        return repository.findParents();
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
