package br.com.zupacademy.propostas.proposta.avaliacao;

import br.com.zupacademy.propostas.proposta.EstadoProposta;

public enum EnumAvaliacaoFinanceiraResultado {
    COM_RESTRICAO{
        @Override
        EstadoProposta statusProposta() {
            return EstadoProposta.NAO_ELEGIVEL;
        }
    },
    SEM_RESTRICAO{
        @Override
        EstadoProposta statusProposta() {
            return EstadoProposta.ELEGIVEL;
        }
    };

    abstract EstadoProposta statusProposta();
}
