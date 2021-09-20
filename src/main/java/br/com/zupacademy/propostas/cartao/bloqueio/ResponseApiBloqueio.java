package br.com.zupacademy.propostas.cartao.bloqueio;

public class ResponseApiBloqueio {

    private String resultado;

    public ResponseApiBloqueio(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }
}
