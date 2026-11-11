package br.com.ecoponto.domain.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CIDADAO")
public class Cidadao extends Usuario {
    protected Cidadao() {
    }

    public Cidadao(String nome, String email, String senhaHash) {
        super(nome, email, senhaHash);
    }

    @Override
    public String getTipoUsuario() {
        return "CIDADAO";
    }
}
