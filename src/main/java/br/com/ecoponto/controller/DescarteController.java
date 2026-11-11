package br.com.ecoponto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ecoponto.dto.ApiDtos.DescarteRequest;
import br.com.ecoponto.dto.ApiDtos.DescarteResponse;
import br.com.ecoponto.service.DescarteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/descartes")
public class DescarteController {
    private final DescarteService descarteService;

    public DescarteController(DescarteService descarteService) {
        this.descarteService = descarteService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CIDADAO')")
    public ResponseEntity<DescarteResponse> registrar(Authentication authentication,
                                                       @Valid @RequestBody DescarteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(descarteService.registrar(authentication.getName(), request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CIDADAO', 'ADMINISTRADOR')")
    public List<DescarteResponse> listar(Authentication authentication) {
        boolean administrador = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
        return administrador ? descarteService.listarTodos() : descarteService.listarDoUsuario(authentication.getName());
    }
}
