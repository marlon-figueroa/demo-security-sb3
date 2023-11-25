package org.app.security.repository;

import org.app.security.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("select c from Categoria c where c.subcategoria is null")
    List<Categoria> findParents();

    @Query("select c from Categoria c where c.titulo=:titulo")
    Optional<Categoria> findByTitulo(String titulo);

}