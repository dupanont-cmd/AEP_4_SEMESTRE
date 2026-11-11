package br.com.ecoponto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ecoponto.dto.ApiDtos.EcopontoRequest;
import br.com.ecoponto.dto.ApiDtos.EcopontoResponse;
import br.com.ecoponto.dto.ApiDtos.MensagemResponse;
import br.com.ecoponto.service.EcopontoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ecopontos")
public class EcopontoController {
    private final EcopontoService ecopontoService;

    public EcopontoController(EcopontoService ecopontoService) {
        this.ecopontoService = ecopontoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EcopontoResponse> criar(@Valid @RequestBody EcopontoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ecopontoService.criar(request));
    }

    @GetMapping
    public List<EcopontoResponse> listar() {
        return ecopontoService.listar();
    }

    @GetMapping("/{id}")
    public EcopontoResponse buscar(@PathVariable Long id) {
        return ecopontoService.buscar(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public EcopontoResponse atualizar(@PathVariable Long id, @Valid @RequestBody EcopontoRequest request) {
        return ecopontoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MensagemResponse> excluir(@PathVariable Long id) {
        ecopontoService.excluir(id);
        return ResponseEntity.ok(new MensagemResponse("Ecoponto excluído com sucesso."));
    }
}
