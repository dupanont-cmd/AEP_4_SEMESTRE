package br.com.ecoponto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ecoponto.dto.ApiDtos.ResgateResponse;
import br.com.ecoponto.service.ResgateService;

@RestController
@RequestMapping("/api/resgates")
@PreAuthorize("hasRole('CIDADAO')")
public class ResgateController {
    private final ResgateService resgateService;

    public ResgateController(ResgateService resgateService) {
        this.resgateService = resgateService;
    }

    @PostMapping("/{recompensaId}")
    public ResponseEntity<ResgateResponse> resgatar(@PathVariable Long recompensaId, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resgateService.resgatar(authentication.getName(), recompensaId));
    }

    @GetMapping
    public List<ResgateResponse> listar(Authentication authentication) {
        return resgateService.listarDoUsuario(authentication.getName());
    }
}
