package br.com.ecoponto.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ecoponto.domain.entity.Descarte;
import br.com.ecoponto.domain.enums.TipoResiduo;
import br.com.ecoponto.repository.DescarteRepository;

@Service
public class RelatorioService {
    private final DescarteRepository descarteRepository;

    public RelatorioService(DescarteRepository descarteRepository) {
        this.descarteRepository = descarteRepository;
    }

    @Transactional(readOnly = true)
    public byte[] gerarCsv(LocalDateTime inicio, LocalDateTime fim, TipoResiduo tipo, Long ecopontoId) {
        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final.");
        }
        List<Descarte> descartes = descarteRepository.filtrarRelatorio(inicio, fim, tipo, ecopontoId);
        StringBuilder csv = new StringBuilder("id;data_hora;cidadao;ecoponto;tipo_residuo;peso_kg;pontos_gerados\n");
        for (Descarte d : descartes) {
            csv.append(d.getId()).append(';')
                    .append(d.getDataHora()).append(';')
                    .append(campoCsv(d.getUsuario().getNome())).append(';')
                    .append(campoCsv(d.getCompartimento().getEcoponto().getNome())).append(';')
                    .append(d.getTipoResiduo()).append(';')
                    .append(d.getPesoKg()).append(';')
                    .append(d.getPontosGerados()).append('\n');
        }
        // BOM UTF-8 facilita a abertura do arquivo em planilhas que esperam essa assinatura.
        return ("\uFEFF" + csv).getBytes(StandardCharsets.UTF_8);
    }

    private String campoCsv(String valor) {
        if (valor == null) return "";
        return "\"" + valor.replace("\"", "\"\"") + "\"";
    }
}
