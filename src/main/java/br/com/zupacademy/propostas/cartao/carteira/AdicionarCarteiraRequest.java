package br.com.zupacademy.propostas.cartao.carteira;

import br.com.zupacademy.propostas.cartao.Cartao;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Map;

public class AdicionarCarteiraRequest {

    @NotBlank @Email @JsonProperty(value = "email")
    private String email;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AdicionarCarteiraRequest(String email) {
        this.email = email;
    }

    public Map<String, String> corpoAvisoSistemaLegado() {
        return Map.of("email",this.email,"carteira","PAYPAL");
    }

    public String getEmail() {
        return email;
    }

    public CarteiraDigitalPaypal toModel(String id, Cartao cartao) {
        return new CarteiraDigitalPaypal(id,this.email,cartao);
    }
}
