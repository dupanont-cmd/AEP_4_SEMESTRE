package br.com.ecoponto.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ecoponto.domain.entity.Parceiro;
import br.com.ecoponto.domain.entity.Recompensa;
import br.com.ecoponto.domain.entity.Usuario;
import br.com.ecoponto.dto.ApiDtos.RecompensaRequest;
import br.com.ecoponto.dto.ApiDtos.RecompensaResponse;
import br.com.ecoponto.repository.RecompensaRepository;
import br.com.ecoponto.repository.UsuarioRepository;

@Service
public class RecompensaService {
    private final RecompensaRepository recompensaRepository;
    private final UsuarioRepository usuarioRepository;

    public RecompensaService(RecompensaRepository recompensaRepository, UsuarioRepository usuarioRepository) {
        this.recompensaRepository = recompensaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public RecompensaResponse criar(String emailParceiro, RecompensaRequest request) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(emailParceiro)
                .orElseThrow(() -> new IllegalArgumentException("Parceiro não encontrado."));
        if (!(usuario instanceof Parceiro parceiro)) {
            throw new IllegalArgumentException("Somente parceiros podem cadastrar recompensas.");
        }
        return converter(recompensaRepository.save(new Recompensa(parceiro,
                request.descricao(), request.custoPontos())));
    }

    @Transactional(readOnly = true)
    public List<RecompensaResponse> listarAtivas() {
        return recompensaRepository.findByAtivaTrueOrderByCustoPontosAsc().stream().map(this::converter).toList();
    }

    @Transactional(readOnly = true)
    public List<RecompensaResponse> listarDoParceiro(String email) {
        return recompensaRepository.findByParceiroEmailIgnoreCaseOrderByIdDesc(email).stream().map(this::converter).toList();
    }

    @Transactional
    public void desativar(Long id, String emailParceiro) {
        Recompensa recompensa = recompensaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recompensa não encontrada."));
        if (!recompensa.getParceiro().getEmail().equalsIgnoreCase(emailParceiro)) {
            throw new IllegalArgumentException("O parceiro só pode desativar suas próprias recompensas.");
        }
        recompensa.desativar();
    }

    private RecompensaResponse converter(Recompensa r) {
        return new RecompensaResponse(r.getId(), r.getParceiro().getId(), r.getParceiro().getNome(),
                r.getDescricao(), r.getCustoPontos(), r.getAtiva());
    }
}
