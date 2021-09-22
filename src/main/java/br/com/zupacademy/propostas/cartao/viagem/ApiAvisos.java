package br.com.zupacademy.propostas.cartao.viagem;

import br.com.zupacademy.propostas.cartao.bloqueio.ResponseApiBloqueio;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(url = "${feign.client.apiCartoes.url}", name = "apiAvisos")
public interface ApiAvisos {

    @PostMapping(value = "/{id}/avisos")
    ResponseApiAviso avisarSistemaLegado(@PathVariable String id, @RequestBody Map<String,String> corpo);

}
