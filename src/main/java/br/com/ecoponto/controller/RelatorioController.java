package br.com.ecoponto.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ecoponto.domain.enums.TipoResiduo;
import br.com.ecoponto.service.RelatorioService;

@RestController
@RequestMapping("/api/relatorios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class RelatorioController {
    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping(value = "/descartes.csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportarDescartes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(required = false) TipoResiduo tipoResiduo,
            @RequestParam(required = false) Long ecopontoId) {
        byte[] arquivo = relatorioService.gerarCsv(inicio, fim, tipoResiduo, ecopontoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-descartes.csv")
                .contentType(new MediaType("text", "csv"))
                .body(arquivo);
    }
}
