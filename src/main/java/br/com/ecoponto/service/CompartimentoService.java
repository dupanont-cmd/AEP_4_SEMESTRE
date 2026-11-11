package br.com.ecoponto.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ecoponto.domain.entity.Compartimento;
import br.com.ecoponto.domain.entity.Ecoponto;
import br.com.ecoponto.dto.ApiDtos.CompartimentoRequest;
import br.com.ecoponto.dto.ApiDtos.CompartimentoResponse;
import br.com.ecoponto.repository.CompartimentoRepository;

@Service
public class CompartimentoService {
    private final CompartimentoRepository compartimentoRepository;
    private final EcopontoService ecopontoService;

    public CompartimentoService(CompartimentoRepository compartimentoRepository, EcopontoService ecopontoService) {
        this.compartimentoRepository = compartimentoRepository;
        this.ecopontoService = ecopontoService;
    }

    @Transactional
    public CompartimentoResponse criar(Long ecopontoId, CompartimentoRequest request) {
        Ecoponto ecoponto = ecopontoService.obterEntidade(ecopontoId);
        Compartimento compartimento = new Compartimento(request.tipoResiduo(), request.capacidadeMaxima(),
                request.alertaPercentual());
        ecoponto.adicionarCompartimento(compartimento);
        return converter(compartimentoRepository.save(compartimento));
    }

    @Transactional(readOnly = true)
    public List<CompartimentoResponse> listarPorEcoponto(Long ecopontoId) {
        ecopontoService.obterEntidade(ecopontoId);
        return compartimentoRepository.findByEcopontoId(ecopontoId).stream().map(this::converter).toList();
    }

    @Transactional
    public CompartimentoResponse atualizarNivel(Long compartimentoId, BigDecimal novoNivel) {
        Compartimento compartimento = obter(compartimentoId);
        compartimento.atualizarNivel(novoNivel);
        return converter(compartimentoRepository.save(compartimento));
    }

    @Transactional(readOnly = true)
    public Compartimento obter(Long id) {
        return compartimentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compartimento não encontrado: " + id));
    }

    private CompartimentoResponse converter(Compartimento c) {
        BigDecimal percentual = c.getNivelAtual().multiply(new BigDecimal("100"))
                .divide(c.getCapacidadeMaxima(), 2, RoundingMode.HALF_UP);
        return new CompartimentoResponse(c.getId(), c.getEcoponto().getId(), c.getTipoResiduo(),
                c.getCapacidadeMaxima(), c.getNivelAtual(), percentual, c.getAlertaPercentual(),
                c.getStatus(), c.deveAlertar());
    }
}
