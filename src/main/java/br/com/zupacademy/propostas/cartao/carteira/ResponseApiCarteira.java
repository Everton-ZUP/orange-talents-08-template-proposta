package br.com.zupacademy.propostas.cartao.carteira;

public class ResponseApiCarteira {

    private String resultado;
    private String id;

    public ResponseApiCarteira(String resultado, String id) {
        this.resultado = resultado;
        this.id = id;
    }

    public String getResultado() {
        return resultado;
    }

    public String getId() {
        return id;
    }
}
