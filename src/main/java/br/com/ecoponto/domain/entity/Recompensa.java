package br.com.ecoponto.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recompensas")
public class Recompensa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parceiro_id", nullable = false)
    private Parceiro parceiro;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "custo_pontos", nullable = false)
    private Integer custoPontos;

    @Column(nullable = false)
    private Boolean ativa = true;

    protected Recompensa() {
    }

    public Recompensa(Parceiro parceiro, String descricao, Integer custoPontos) {
        this.parceiro = parceiro;
        this.descricao = descricao;
        this.custoPontos = custoPontos;
        this.ativa = true;
    }

    public void desativar() { this.ativa = false; }
    public Long getId() { return id; }
    public Parceiro getParceiro() { return parceiro; }
    public String getDescricao() { return descricao; }
    public Integer getCustoPontos() { return custoPontos; }
    public Boolean getAtiva() { return ativa; }
}
