package br.com.ecoponto.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ecoponto.domain.entity.Cidadao;
import br.com.ecoponto.domain.entity.Compartimento;
import br.com.ecoponto.domain.entity.Descarte;
import br.com.ecoponto.domain.entity.Usuario;
import br.com.ecoponto.domain.strategy.ResiduoFactory;
import br.com.ecoponto.dto.ApiDtos.DescarteRequest;
import br.com.ecoponto.dto.ApiDtos.DescarteResponse;
import br.com.ecoponto.repository.CompartimentoRepository;
import br.com.ecoponto.repository.DescarteRepository;
import br.com.ecoponto.repository.UsuarioRepository;

@Service
public class DescarteService {
    private final DescarteRepository descarteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CompartimentoRepository compartimentoRepository;
    private final ResiduoFactory residuoFactory;

    public DescarteService(DescarteRepository descarteRepository, UsuarioRepository usuarioRepository,
                           CompartimentoRepository compartimentoRepository, ResiduoFactory residuoFactory) {
        this.descarteRepository = descarteRepository;
        this.usuarioRepository = usuarioRepository;
        this.compartimentoRepository = compartimentoRepository;
        this.residuoFactory = residuoFactory;
    }

    @Transactional
    public DescarteResponse registrar(String email, DescarteRequest request) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        if (!(usuario instanceof Cidadao cidadao)) {
            throw new IllegalArgumentException("Somente cidadãos podem registrar descartes.");
        }

        Compartimento compartimento = compartimentoRepository.findById(request.compartimentoId())
                .orElseThrow(() -> new IllegalArgumentException("Compartimento não encontrado."));
        if (compartimento.getTipoResiduo() != request.tipoResiduo()) {
            throw new IllegalArgumentException("O tipo de resíduo não corresponde ao compartimento selecionado.");
        }

        int pontos = residuoFactory.obterEstrategia(request.tipoResiduo()).calcularPontuacao(request.pesoKg());
        compartimento.adicionarCarga(request.pesoKg());
        cidadao.adicionarPontos(pontos);

        Compartimento compartimentoAtualizado = compartimentoRepository.save(compartimento);
        Usuario usuarioAtualizado = usuarioRepository.save(cidadao);
        Descarte descarte = descarteRepository.save(new Descarte(cidadao, compartimentoAtualizado,
                request.tipoResiduo(), request.pesoKg(), pontos));
        return converter(descarte, usuarioAtualizado.getSaldoPontos());
    }

    @Transactional(readOnly = true)
    public List<DescarteResponse> listarDoUsuario(String email) {
        return descarteRepository.findByUsuarioEmailIgnoreCaseOrderByDataHoraDesc(email).stream()
                .map(d -> converter(d, d.getUsuario().getSaldoPontos())).toList();
    }

    @Transactional(readOnly = true)
    public List<DescarteResponse> listarTodos() {
        return descarteRepository.findAll().stream()
                .map(d -> converter(d, d.getUsuario().getSaldoPontos())).toList();
    }

    private DescarteResponse converter(Descarte d, Integer saldo) {
        return new DescarteResponse(d.getId(), d.getUsuario().getId(), d.getUsuario().getNome(),
                d.getCompartimento().getEcoponto().getId(), d.getCompartimento().getEcoponto().getNome(),
                d.getCompartimento().getId(), d.getTipoResiduo(), d.getDataHora(), d.getPesoKg(),
                d.getPontosGerados(), saldo);
    }
}
