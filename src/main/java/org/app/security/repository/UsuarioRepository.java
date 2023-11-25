package org.app.security.repository;

import java.util.Optional;

import org.app.security.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("select u from Usuario u where u.email like :email")
	Usuario findByEmail(@Param("email") String email);

	@Query("select distinct u from Usuario u " + "join u.perfil p "
			+ "where u.email like :search% OR p.desc like :search%")
	Page<Usuario> findByEmailOrPerfil(String search, Pageable pageable);

	@Query("select u from Usuario u " + "join u.perfil p " + "where u.id = :usuarioId AND p.id IN :perfilId")
	Optional<Usuario> findByIdAndPerfil(Long usuarioId, Long[] perfilId);

	@Query("select u from Usuario u where u.email like :email AND u.activo = true")
	Optional<Usuario> findByEmailAndActivo(String email);
}
