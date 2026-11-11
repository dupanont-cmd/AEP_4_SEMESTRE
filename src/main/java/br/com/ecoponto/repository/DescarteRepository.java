package br.com.ecoponto.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.com.ecoponto.domain.entity.Descarte;
import br.com.ecoponto.domain.enums.TipoResiduo;

public interface DescarteRepository extends JpaRepository<Descarte, Long> {
    List<Descarte> findByUsuarioEmailIgnoreCaseOrderByDataHoraDesc(String email);
    boolean existsByCompartimento_Ecoponto_Id(Long ecopontoId);

    @Query("""
           select d from Descarte d
           where (:inicio is null or d.dataHora >= :inicio)
             and (:fim is null or d.dataHora <= :fim)
             and (:tipo is null or d.tipoResiduo = :tipo)
             and (:ecopontoId is null or d.compartimento.ecoponto.id = :ecopontoId)
           order by d.dataHora desc
           """)
    List<Descarte> filtrarRelatorio(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("tipo") TipoResiduo tipo,
            @Param("ecopontoId") Long ecopontoId);
}
