package br.com.ecoponto.domain.strategy;

import java.math.BigDecimal;

import br.com.ecoponto.domain.enums.TipoResiduo;

/** Estratégia polimórfica de cálculo dos pontos de cada categoria de resíduo. */
public interface Residuo {
    TipoResiduo getTipo();
    int calcularPontuacao(BigDecimal pesoKg);
}
