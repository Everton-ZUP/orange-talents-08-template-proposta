package br.com.zupacademy.propostas.cartao.viagem;

import br.com.zupacademy.propostas.cartao.Cartao;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class AvisoViagemRequest {

    @NotNull @NotBlank
    private String destinoViagem;
    @NotNull @Future
    private LocalDate terminoViagem;

    public AvisoViagemRequest(String destinoViagem, LocalDate terminoViagem) {
        this.destinoViagem = destinoViagem;
        this.terminoViagem = terminoViagem;
    }

    public AvisoViagem toModel(HttpServletRequest request, Cartao cartao) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String userAgent = request.getHeader("User-Agent");

        return new AvisoViagem(this.destinoViagem,ipAddress,userAgent,this.terminoViagem,cartao);
    }

    public Map<String, String> retornaCorpoRequisicaoApiExterna() {
        return Map.of("destino",this.destinoViagem,"validoAte",this.terminoViagem.toString());
    }
}
