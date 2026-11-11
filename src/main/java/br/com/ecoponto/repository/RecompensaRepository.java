package br.com.ecoponto.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ecoponto.domain.entity.Recompensa;

public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {
    List<Recompensa> findByAtivaTrueOrderByCustoPontosAsc();
    List<Recompensa> findByParceiroEmailIgnoreCaseOrderByIdDesc(String email);
}
