package br.com.zupacademy.propostas.exception;

public class ErroRegraDeNegocio extends RuntimeException {
    private ReturnError erroDeRetorno;

    public ErroRegraDeNegocio(String mensagem, String campo, Object value ) {
        erroDeRetorno = new ReturnError();
        erroDeRetorno.addErrorField(campo,value,mensagem,"","");
    }

    public ReturnError getErroDeRetorno() {
        return erroDeRetorno;
    }
}
