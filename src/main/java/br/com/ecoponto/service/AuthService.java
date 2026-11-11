package br.com.ecoponto.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ecoponto.domain.entity.Cidadao;
import br.com.ecoponto.domain.entity.Parceiro;
import br.com.ecoponto.domain.entity.Usuario;
import br.com.ecoponto.dto.ApiDtos.CadastroRequest;
import br.com.ecoponto.dto.ApiDtos.UsuarioResponse;
import br.com.ecoponto.repository.UsuarioRepository;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse cadastrar(CadastroRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("Já existe uma conta cadastrada com esse e-mail.");
        }

        String senhaHash = passwordEncoder.encode(request.senha());
        Usuario usuario;
        if (request.perfil().name().equals("PARCEIRO")) {
            if (request.cnpj() == null || request.cnpj().isBlank()) {
                throw new IllegalArgumentException("Informe o CNPJ para cadastrar um parceiro.");
            }
            usuario = new Parceiro(request.nome(), request.email().trim().toLowerCase(), senhaHash, request.cnpj());
        } else {
            usuario = new Cidadao(request.nome(), request.email().trim().toLowerCase(), senhaHash);
        }
        return converter(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse consultarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        return converter(usuario);
    }

    public UsuarioResponse converter(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(),
                usuario.getTipoUsuario(), usuario.getSaldoPontos());
    }
}
