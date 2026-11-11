package br.com.ecoponto.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ecoponto.domain.entity.Compartimento;
import br.com.ecoponto.domain.entity.Ecoponto;
import br.com.ecoponto.dto.ApiDtos.CompartimentoResponse;
import br.com.ecoponto.dto.ApiDtos.EcopontoRequest;
import br.com.ecoponto.dto.ApiDtos.EcopontoResponse;
import br.com.ecoponto.repository.DescarteRepository;
import br.com.ecoponto.repository.EcopontoRepository;

@Service
public class EcopontoService {
    private final EcopontoRepository ecopontoRepository;
    private final DescarteRepository descarteRepository;

    public EcopontoService(EcopontoRepository ecopontoRepository, DescarteRepository descarteRepository) {
        this.ecopontoRepository = ecopontoRepository;
        this.descarteRepository = descarteRepository;
    }

    @Transactional
    public EcopontoResponse criar(EcopontoRequest request) {
        Ecoponto ecoponto = new Ecoponto(request.nome(), request.endereco(), request.latitude(), request.longitude());
        return converter(ecopontoRepository.save(ecoponto));
    }

    @Transactional(readOnly = true)
    public List<EcopontoResponse> listar() {
        return ecopontoRepository.findAll().stream().map(this::converter).toList();
    }

    @Transactional(readOnly = true)
    public EcopontoResponse buscar(Long id) {
        return converter(obterEntidade(id));
    }

    @Transactional
    public EcopontoResponse atualizar(Long id, EcopontoRequest request) {
        Ecoponto ecoponto = obterEntidade(id);
        ecoponto.atualizar(request.nome(), request.endereco(), request.latitude(), request.longitude());
        return converter(ecopontoRepository.save(ecoponto));
    }

    @Transactional
    public void excluir(Long id) {
        Ecoponto ecoponto = obterEntidade(id);
        if (descarteRepository.existsByCompartimento_Ecoponto_Id(id)) {
            throw new IllegalArgumentException("Não é possível excluir um ecoponto que possui histórico de descartes.");
        }
        ecopontoRepository.delete(ecoponto);
    }

    @Transactional(readOnly = true)
    public Ecoponto obterEntidade(Long id) {
        return ecopontoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ecoponto não encontrado: " + id));
    }

    private EcopontoResponse converter(Ecoponto ecoponto) {
        List<CompartimentoResponse> compartimentos = ecoponto.getCompartimentos().stream()
                .map(this::converterCompartimento).toList();
        return new EcopontoResponse(ecoponto.getId(), ecoponto.getNome(), ecoponto.getEndereco(),
                ecoponto.getLatitude(), ecoponto.getLongitude(), compartimentos);
    }

    private CompartimentoResponse converterCompartimento(Compartimento compartimento) {
        BigDecimal percentual = compartimento.getNivelAtual()
                .multiply(new BigDecimal("100"))
                .divide(compartimento.getCapacidadeMaxima(), 2, RoundingMode.HALF_UP);
        return new CompartimentoResponse(compartimento.getId(), compartimento.getEcoponto().getId(),
                compartimento.getTipoResiduo(), compartimento.getCapacidadeMaxima(), compartimento.getNivelAtual(),
                percentual, compartimento.getAlertaPercentual(), compartimento.getStatus(), compartimento.deveAlertar());
    }
}
