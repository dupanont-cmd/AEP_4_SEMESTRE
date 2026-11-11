package br.com.ecoponto.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ecoponto.domain.entity.Compartimento;

public interface CompartimentoRepository extends JpaRepository<Compartimento, Long> {
    List<Compartimento> findByEcopontoId(Long ecopontoId);
    Optional<Compartimento> findByIdAndEcopontoId(Long id, Long ecopontoId);
}
