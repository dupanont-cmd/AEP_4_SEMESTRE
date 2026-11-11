package br.com.ecoponto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ecoponto.domain.entity.Resgate;

public interface ResgateRepository extends JpaRepository<Resgate, Long> {
    List<Resgate> findByUsuarioEmailIgnoreCaseOrderByDataHoraDesc(String email);
}
