package br.com.zupacademy.propostas.biometria.cadastro;

import br.com.zupacademy.propostas.biometria.Biometria;
import br.com.zupacademy.propostas.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RequestBiometria {

    @JsonProperty
    @NotBlank @NotNull
    private String fingerPrint;

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public Biometria toBiometria(Cartao cartao) {
        return new Biometria(fingerPrint,cartao);
    }
}
