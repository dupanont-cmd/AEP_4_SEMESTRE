package br.com.ecoponto.domain.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.com.ecoponto.domain.enums.TipoResiduo;

class ResiduoPontuacaoTest {
    @Test
    void deveCalcularPontuacaoDeReciclavel() {
        Residuo estrategia = new ResiduoReciclavel();
        assertEquals(TipoResiduo.RECICLAVEL, estrategia.getTipo());
        assertEquals(25, estrategia.calcularPontuacao(new BigDecimal("2.5")));
    }

    @Test
    void deveCalcularPontuacaoDeEletronico() {
        Residuo estrategia = new ResiduoEletronico();
        assertEquals(TipoResiduo.ELETRONICO, estrategia.getTipo());
        assertEquals(50, estrategia.calcularPontuacao(new BigDecimal("2.5")));
    }

    @Test
    void deveCalcularPontuacaoDeOrganico() {
        Residuo estrategia = new ResiduoOrganico();
        assertEquals(TipoResiduo.ORGANICO, estrategia.getTipo());
        assertEquals(13, estrategia.calcularPontuacao(new BigDecimal("2.5")));
    }

    @Test
    void naoDeveAceitarPesoZeroOuNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new ResiduoReciclavel().calcularPontuacao(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new ResiduoEletronico().calcularPontuacao(new BigDecimal("-1")));
    }
}
