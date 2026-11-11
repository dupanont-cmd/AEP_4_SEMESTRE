package br.com.ecoponto.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CidadaoTest {
    @Test
    void deveCreditarEDebitarPontos() {
        Cidadao cidadao = new Cidadao("Ana", "ana@example.com", "hash");
        cidadao.adicionarPontos(30);
        cidadao.debitarPontos(10);
        assertEquals(20, cidadao.getSaldoPontos());
    }

    @Test
    void naoDevePermitirResgateComSaldoInsuficiente() {
        Cidadao cidadao = new Cidadao("Ana", "ana@example.com", "hash");
        assertThrows(IllegalArgumentException.class, () -> cidadao.debitarPontos(1));
    }
}
