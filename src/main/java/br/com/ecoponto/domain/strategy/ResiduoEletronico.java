package br.com.ecoponto.domain.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import br.com.ecoponto.domain.enums.TipoResiduo;

@Component
public class ResiduoEletronico implements Residuo {
    private static final BigDecimal PONTOS_POR_KG = new BigDecimal("20");

    @Override
    public TipoResiduo getTipo() {
        return TipoResiduo.ELETRONICO;
    }

    @Override
    public int calcularPontuacao(BigDecimal pesoKg) {
        if (pesoKg == null || pesoKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O peso deve ser maior que zero.");
        }
        return pesoKg.multiply(PONTOS_POR_KG).setScale(0, RoundingMode.HALF_UP).intValueExact();
    }
}
