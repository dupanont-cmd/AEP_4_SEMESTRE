package br.com.ecoponto.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PARCEIRO")
public class Parceiro extends Usuario {
    @Column(name = "cnpj", length = 30)
    private String cnpj;

    protected Parceiro() {
    }

    public Parceiro(String nome, String email, String senhaHash, String cnpj) {
        super(nome, email, senhaHash);
        this.cnpj = cnpj;
    }

    @Override
    public String getTipoUsuario() {
        return "PARCEIRO";
    }

    public String getCnpj() {
        return cnpj;
    }
}
