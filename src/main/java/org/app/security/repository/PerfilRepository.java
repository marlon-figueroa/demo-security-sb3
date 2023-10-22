package org.app.security.repository;

import java.util.Optional;

import org.app.security.domain.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

	Optional<Perfil> findByDesc(String desc);

}
