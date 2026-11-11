package br.com.ecoponto.domain.entity;

import java.time.LocalDateTime;

import br.com.ecoponto.domain.enums.StatusResgate;
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
@Table(name = "resgates")
public class Resgate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Cidadao usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recompensa_id", nullable = false)
    private Recompensa recompensa;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "pontos_usados", nullable = false)
    private Integer pontosUsados;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusResgate status;

    protected Resgate() {
    }

    public Resgate(Cidadao usuario, Recompensa recompensa) {
        this.usuario = usuario;
        this.recompensa = recompensa;
        this.dataHora = LocalDateTime.now();
        this.pontosUsados = recompensa.getCustoPontos();
        this.status = StatusResgate.CONFIRMADO;
    }

    public Long getId() { return id; }
    public Cidadao getUsuario() { return usuario; }
    public Recompensa getRecompensa() { return recompensa; }
    public LocalDateTime getDataHora() { return dataHora; }
    public Integer getPontosUsados() { return pontosUsados; }
    public StatusResgate getStatus() { return status; }
}
