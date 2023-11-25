package org.app.security.repository;

import org.app.security.domain.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("select c from Categoria c where c.subcategoria is null")
    List<Categoria> findParents();

    @Query("select c from Categoria c left join c.subcategoria s where c.titulo like :search%")
	Page<Categoria> findAllByTitulo(String search, Pageable pageable);

    @Query("select c from Categoria c where c.titulo=:titulo")
    Optional<Categoria> findByTitulo(String titulo);

}