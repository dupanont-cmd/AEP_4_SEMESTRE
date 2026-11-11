package br.com.ecoponto.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ecoponto.domain.entity.Cidadao;
import br.com.ecoponto.domain.entity.Recompensa;
import br.com.ecoponto.domain.entity.Resgate;
import br.com.ecoponto.domain.entity.Usuario;
import br.com.ecoponto.dto.ApiDtos.ResgateResponse;
import br.com.ecoponto.repository.RecompensaRepository;
import br.com.ecoponto.repository.ResgateRepository;
import br.com.ecoponto.repository.UsuarioRepository;

@Service
public class ResgateService {
    private final ResgateRepository resgateRepository;
    private final RecompensaRepository recompensaRepository;
    private final UsuarioRepository usuarioRepository;

    public ResgateService(ResgateRepository resgateRepository, RecompensaRepository recompensaRepository,
                          UsuarioRepository usuarioRepository) {
        this.resgateRepository = resgateRepository;
        this.recompensaRepository = recompensaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ResgateResponse resgatar(String email, Long recompensaId) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        if (!(usuario instanceof Cidadao cidadao)) {
            throw new IllegalArgumentException("Somente cidadãos podem resgatar recompensas.");
        }
        Recompensa recompensa = recompensaRepository.findById(recompensaId)
                .orElseThrow(() -> new IllegalArgumentException("Recompensa não encontrada."));
        if (!Boolean.TRUE.equals(recompensa.getAtiva())) {
            throw new IllegalArgumentException("Esta recompensa não está mais disponível.");
        }

        cidadao.debitarPontos(recompensa.getCustoPontos());
        usuarioRepository.save(cidadao);
        Resgate resgate = resgateRepository.save(new Resgate(cidadao, recompensa));
        return converter(resgate, cidadao.getSaldoPontos());
    }

    @Transactional(readOnly = true)
    public List<ResgateResponse> listarDoUsuario(String email) {
        return resgateRepository.findByUsuarioEmailIgnoreCaseOrderByDataHoraDesc(email).stream()
                .map(r -> converter(r, r.getUsuario().getSaldoPontos())).toList();
    }

    private ResgateResponse converter(Resgate r, Integer saldo) {
        return new ResgateResponse(r.getId(), r.getRecompensa().getId(), r.getRecompensa().getDescricao(),
                r.getDataHora(), r.getPontosUsados(), r.getStatus(), saldo);
    }
}
