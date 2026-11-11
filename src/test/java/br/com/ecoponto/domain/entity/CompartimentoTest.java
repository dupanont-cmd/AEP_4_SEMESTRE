package br.com.ecoponto.domain.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.com.ecoponto.domain.enums.TipoResiduo;

class CompartimentoTest {
    @Test
    void deveSinalizarAlertaAoAtingirPercentualConfigurado() {
        Compartimento compartimento = new Compartimento(TipoResiduo.RECICLAVEL,
                new BigDecimal("100"), new BigDecimal("80"));

        compartimento.atualizarNivel(new BigDecimal("79.9"));
        assertFalse(compartimento.deveAlertar());

        compartimento.atualizarNivel(new BigDecimal("80"));
        assertTrue(compartimento.deveAlertar());
    }

    @Test
    void naoDevePermitirUltrapassarCapacidade() {
        Compartimento compartimento = new Compartimento(TipoResiduo.ORGANICO,
                new BigDecimal("10"), new BigDecimal("80"));

        assertThrows(IllegalArgumentException.class,
                () -> compartimento.adicionarCarga(new BigDecimal("10.001")));
    }
}
