package br.com.ecoponto.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ecoponto.domain.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
