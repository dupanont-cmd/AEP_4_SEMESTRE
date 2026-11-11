package br.com.ecoponto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ecoponto.dto.ApiDtos.MensagemResponse;
import br.com.ecoponto.dto.ApiDtos.RecompensaRequest;
import br.com.ecoponto.dto.ApiDtos.RecompensaResponse;
import br.com.ecoponto.service.RecompensaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recompensas")
public class RecompensaController {
    private final RecompensaService recompensaService;

    public RecompensaController(RecompensaService recompensaService) {
        this.recompensaService = recompensaService;
    }

    @GetMapping
    public List<RecompensaResponse> listarAtivas() {
        return recompensaService.listarAtivas();
    }

    @PostMapping
    @PreAuthorize("hasRole('PARCEIRO')")
    public ResponseEntity<RecompensaResponse> criar(Authentication authentication,
                                                     @Valid @RequestBody RecompensaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recompensaService.criar(authentication.getName(), request));
    }

    @GetMapping("/minhas")
    @PreAuthorize("hasRole('PARCEIRO')")
    public List<RecompensaResponse> minhas(Authentication authentication) {
        return recompensaService.listarDoParceiro(authentication.getName());
    }

    @PatchMapping("/{id}/desativar")
    @PreAuthorize("hasRole('PARCEIRO')")
    public ResponseEntity<MensagemResponse> desativar(@PathVariable Long id, Authentication authentication) {
        recompensaService.desativar(id, authentication.getName());
        return ResponseEntity.ok(new MensagemResponse("Recompensa desativada."));
    }
}
