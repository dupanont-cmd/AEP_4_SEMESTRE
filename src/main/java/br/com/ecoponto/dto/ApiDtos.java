package br.com.ecoponto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import br.com.ecoponto.domain.enums.PerfilCadastro;
import br.com.ecoponto.domain.enums.StatusResgate;
import br.com.ecoponto.domain.enums.TipoResiduo;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class ApiDtos {
    private ApiDtos() {
    }

    public record CadastroRequest(
            @NotBlank @Size(max = 120) String nome,
            @NotBlank @Email @Size(max = 120) String email,
            @NotBlank @Size(min = 8, max = 72) String senha,
            @NotNull PerfilCadastro perfil,
            @Size(max = 30) String cnpj) {
    }

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String senha) {
    }

    public record UsuarioResponse(Long id, String nome, String email, String tipoUsuario, Integer saldoPontos) {
    }

    public record EcopontoRequest(
            @NotBlank @Size(max = 120) String nome,
            @NotBlank @Size(max = 255) String endereco,
            BigDecimal latitude,
            BigDecimal longitude) {
    }

    public record EcopontoResponse(
            Long id, String nome, String endereco, BigDecimal latitude, BigDecimal longitude,
            List<CompartimentoResponse> compartimentos) {
    }

    public record CompartimentoRequest(
            @NotNull TipoResiduo tipoResiduo,
            @NotNull @DecimalMin("0.001") BigDecimal capacidadeMaxima,
            @DecimalMin("1.00") @DecimalMax("100.00") BigDecimal alertaPercentual) {
    }

    public record NivelRequest(@NotNull @DecimalMin("0.000") BigDecimal nivelAtual) {
    }

    public record CompartimentoResponse(
            Long id, Long ecopontoId, TipoResiduo tipoResiduo, BigDecimal capacidadeMaxima,
            BigDecimal nivelAtual, BigDecimal percentualAtual, BigDecimal alertaPercentual,
            String status, boolean alertaColeta) {
    }

    public record DescarteRequest(
            @NotNull Long compartimentoId,
            @NotNull TipoResiduo tipoResiduo,
            @NotNull @DecimalMin("0.001") BigDecimal pesoKg) {
    }

    public record DescarteResponse(
            Long id, Long usuarioId, String nomeUsuario, Long ecopontoId, String nomeEcoponto,
            Long compartimentoId, TipoResiduo tipoResiduo, LocalDateTime dataHora,
            BigDecimal pesoKg, Integer pontosGerados, Integer saldoAtual) {
    }

    public record RecompensaRequest(
            @NotBlank @Size(max = 255) String descricao,
            @NotNull @Min(1) Integer custoPontos) {
    }

    public record RecompensaResponse(
            Long id, Long parceiroId, String nomeParceiro, String descricao,
            Integer custoPontos, Boolean ativa) {
    }

    public record ResgateResponse(
            Long id, Long recompensaId, String descricaoRecompensa, LocalDateTime dataHora,
            Integer pontosUsados, StatusResgate status, Integer saldoAtual) {
    }

    public record MensagemResponse(String mensagem) {
    }
}
