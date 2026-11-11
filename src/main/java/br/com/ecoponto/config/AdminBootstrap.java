package br.com.ecoponto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.ecoponto.domain.entity.Administrador;
import br.com.ecoponto.repository.UsuarioRepository;

@Configuration
public class AdminBootstrap {
    @Bean
    CommandLineRunner criarAdministradorInicial(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email}") String email,
            @Value("${app.admin.password}") String senha) {
        return args -> {
            if (!usuarioRepository.existsByEmailIgnoreCase(email)) {
                usuarioRepository.save(new Administrador("Administrador do Sistema", email,
                        passwordEncoder.encode(senha)));
            }
        };
    }
}
