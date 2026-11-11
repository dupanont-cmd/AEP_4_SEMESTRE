package br.com.ecoponto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ecoponto.domain.entity.Ecoponto;

public interface EcopontoRepository extends JpaRepository<Ecoponto, Long> {
}
