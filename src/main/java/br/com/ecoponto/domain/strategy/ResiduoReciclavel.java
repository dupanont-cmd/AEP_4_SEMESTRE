package br.com.ecoponto.domain.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import br.com.ecoponto.domain.enums.TipoResiduo;

@Component
public class ResiduoReciclavel implements Residuo {
    private static final BigDecimal PONTOS_POR_KG = new BigDecimal("10");

    @Override
    public TipoResiduo getTipo() {
        return TipoResiduo.RECICLAVEL;
    }

    @Override
    public int calcularPontuacao(BigDecimal pesoKg) {
        validarPeso(pesoKg);
        return pesoKg.multiply(PONTOS_POR_KG).setScale(0, RoundingMode.HALF_UP).intValueExact();
    }

    private void validarPeso(BigDecimal pesoKg) {
        if (pesoKg == null || pesoKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O peso deve ser maior que zero.");
        }
    }
}
