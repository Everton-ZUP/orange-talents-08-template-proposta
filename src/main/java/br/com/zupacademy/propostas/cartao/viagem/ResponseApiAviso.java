package br.com.zupacademy.propostas.cartao.viagem;

public class ResponseApiAviso {

    private String resultado;

    public boolean sucesso() {
        return resultado.equals("CRIADO");
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
