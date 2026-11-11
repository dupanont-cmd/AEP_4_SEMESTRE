package br.com.ecoponto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ecoponto.dto.ApiDtos.CompartimentoRequest;
import br.com.ecoponto.dto.ApiDtos.CompartimentoResponse;
import br.com.ecoponto.dto.ApiDtos.NivelRequest;
import br.com.ecoponto.service.CompartimentoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CompartimentoController {
    private final CompartimentoService compartimentoService;

    public CompartimentoController(CompartimentoService compartimentoService) {
        this.compartimentoService = compartimentoService;
    }

    @PostMapping("/ecopontos/{ecopontoId}/compartimentos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CompartimentoResponse> criar(@PathVariable Long ecopontoId,
                                                       @Valid @RequestBody CompartimentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compartimentoService.criar(ecopontoId, request));
    }

    @GetMapping("/ecopontos/{ecopontoId}/compartimentos")
    public List<CompartimentoResponse> listar(@PathVariable Long ecopontoId) {
        return compartimentoService.listarPorEcoponto(ecopontoId);
    }

    @PatchMapping("/compartimentos/{id}/nivel")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public CompartimentoResponse atualizarNivel(@PathVariable Long id, @Valid @RequestBody NivelRequest request) {
        return compartimentoService.atualizarNivel(id, request.nivelAtual());
    }
}
