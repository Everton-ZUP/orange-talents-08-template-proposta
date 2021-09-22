package br.com.zupacademy.propostas.cartao.viagem;

public class ResponseApiAviso {

    private AvisoViagemRetornoEnum resultado;

    public ResponseApiAviso(AvisoViagemRetornoEnum resultado) {
        this.resultado = resultado;
    }

    public AvisoViagemRetornoEnum getResultado() {
        return resultado;
    }
}
