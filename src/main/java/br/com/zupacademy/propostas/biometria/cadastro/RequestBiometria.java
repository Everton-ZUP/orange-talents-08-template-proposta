package br.com.zupacademy.propostas.biometria.cadastro;

import br.com.zupacademy.propostas.biometria.Biometria;
import br.com.zupacademy.propostas.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Base64;

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

    public void validarBiometria() {
        try{
            byte[] bites = Base64.getDecoder().decode(this.fingerPrint);
            String teste = Base64.getEncoder().encodeToString(bites);
            Assert.isTrue(teste.equals(this.fingerPrint),"Base64 Inválida");
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"digital inválida");
        }
    }
}
