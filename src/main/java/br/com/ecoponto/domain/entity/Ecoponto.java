package br.com.ecoponto.domain.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "ecopontos")
public class Ecoponto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 255)
    private String endereco;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @OneToMany(mappedBy = "ecoponto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Compartimento> compartimentos = new ArrayList<>();

    protected Ecoponto() {
    }

    public Ecoponto(String nome, String endereco, BigDecimal latitude, BigDecimal longitude) {
        this.nome = nome;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void atualizar(String nome, String endereco, BigDecimal latitude, BigDecimal longitude) {
        this.nome = nome;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void adicionarCompartimento(Compartimento compartimento) {
        compartimentos.add(compartimento);
        compartimento.setEcoponto(this);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public BigDecimal getLatitude() { return latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public List<Compartimento> getCompartimentos() { return compartimentos; }
}
