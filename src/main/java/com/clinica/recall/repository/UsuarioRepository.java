package com.clinica.recall.repository;

import com.clinica.recall.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByTokenConvite(String tokenConvite);

    List<Usuario> findAllByOrderByNomeAsc();
}