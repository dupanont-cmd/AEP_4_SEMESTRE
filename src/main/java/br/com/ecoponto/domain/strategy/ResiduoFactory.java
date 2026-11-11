package br.com.ecoponto.domain.strategy;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.ecoponto.domain.enums.TipoResiduo;

@Component
public class ResiduoFactory {
    private final Map<TipoResiduo, Residuo> estrategias = new EnumMap<>(TipoResiduo.class);

    public ResiduoFactory(List<Residuo> residuos) {
        for (Residuo residuo : residuos) {
            estrategias.put(residuo.getTipo(), residuo);
        }
    }

    public Residuo obterEstrategia(TipoResiduo tipoResiduo) {
        Residuo estrategia = estrategias.get(tipoResiduo);
        if (estrategia == null) {
            throw new IllegalArgumentException("Não existe regra de pontuação para o resíduo informado.");
        }
        return estrategia;
    }
}
