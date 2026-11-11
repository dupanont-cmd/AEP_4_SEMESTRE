package br.com.ecoponto.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Table(name = "descartes")
public class Descarte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Cidadao usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "compartimento_id", nullable = false)
    private Compartimento compartimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_residuo", nullable = false, length = 30)
    private TipoResiduo tipoResiduo;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "peso_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal pesoKg;

    @Column(name = "pontos_gerados", nullable = false)
    private Integer pontosGerados;

    protected Descarte() {
    }

    public Descarte(Cidadao usuario, Compartimento compartimento, TipoResiduo tipoResiduo,
                    BigDecimal pesoKg, Integer pontosGerados) {
        this.usuario = usuario;
        this.compartimento = compartimento;
        this.tipoResiduo = tipoResiduo;
        this.pesoKg = pesoKg;
        this.pontosGerados = pontosGerados;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Cidadao getUsuario() { return usuario; }
    public Compartimento getCompartimento() { return compartimento; }
    public TipoResiduo getTipoResiduo() { return tipoResiduo; }
    public LocalDateTime getDataHora() { return dataHora; }
    public BigDecimal getPesoKg() { return pesoKg; }
    public Integer getPontosGerados() { return pontosGerados; }
}
