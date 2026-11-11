package br.com.ecoponto.domain.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.ecoponto.domain.enums.TipoResiduo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "compartimentos")
public class Compartimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ecoponto_id", nullable = false)
    private Ecoponto ecoponto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_residuo", nullable = false, length = 30)
    private TipoResiduo tipoResiduo;

    @Column(name = "capacidade_maxima", nullable = false, precision = 10, scale = 3)
    private BigDecimal capacidadeMaxima;

    @Column(name = "nivel_atual", nullable = false, precision = 10, scale = 3)
    private BigDecimal nivelAtual = BigDecimal.ZERO;

    @Column(name = "alerta_percentual", nullable = false, precision = 5, scale = 2)
    private BigDecimal alertaPercentual = new BigDecimal("80.00");

    @Column(name = "status", nullable = false, length = 20)
    private String status = "DISPONIVEL";

    protected Compartimento() {
    }

    public Compartimento(TipoResiduo tipoResiduo, BigDecimal capacidadeMaxima, BigDecimal alertaPercentual) {
        this.tipoResiduo = tipoResiduo;
        this.capacidadeMaxima = capacidadeMaxima;
        this.alertaPercentual = alertaPercentual == null ? new BigDecimal("80.00") : alertaPercentual;
        this.nivelAtual = BigDecimal.ZERO;
        this.status = "DISPONIVEL";
        validarConfiguracao();
    }

    private void validarConfiguracao() {
        if (capacidadeMaxima == null || capacidadeMaxima.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A capacidade máxima deve ser maior que zero.");
        }
        if (alertaPercentual.compareTo(BigDecimal.ZERO) <= 0
                || alertaPercentual.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("O percentual de alerta deve estar entre 0 e 100.");
        }
    }

    public void atualizarNivel(BigDecimal novoNivel) {
        if (novoNivel == null || novoNivel.compareTo(BigDecimal.ZERO) < 0
                || novoNivel.compareTo(capacidadeMaxima) > 0) {
            throw new IllegalArgumentException("O nível informado deve estar entre zero e a capacidade máxima.");
        }
        this.nivelAtual = novoNivel.setScale(3, RoundingMode.HALF_UP);
        this.status = deveAlertar() ? "COLETA_NECESSARIA" : "DISPONIVEL";
    }

    public void adicionarCarga(BigDecimal peso) {
        BigDecimal novoNivel = nivelAtual.add(peso);
        if (novoNivel.compareTo(capacidadeMaxima) > 0) {
            throw new IllegalArgumentException("O compartimento não possui capacidade disponível para esse descarte.");
        }
        atualizarNivel(novoNivel);
    }

    public boolean deveAlertar() {
        BigDecimal percentualAtual = nivelAtual.multiply(new BigDecimal("100"))
                .divide(capacidadeMaxima, 2, RoundingMode.HALF_UP);
        return percentualAtual.compareTo(alertaPercentual) >= 0;
    }

    public void setEcoponto(Ecoponto ecoponto) { this.ecoponto = ecoponto; }
    public Long getId() { return id; }
    public Ecoponto getEcoponto() { return ecoponto; }
    public TipoResiduo getTipoResiduo() { return tipoResiduo; }
    public BigDecimal getCapacidadeMaxima() { return capacidadeMaxima; }
    public BigDecimal getNivelAtual() { return nivelAtual; }
    public BigDecimal getAlertaPercentual() { return alertaPercentual; }
    public String getStatus() { return status; }
}
