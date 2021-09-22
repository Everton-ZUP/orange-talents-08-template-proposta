package br.com.zupacademy.propostas.cartao;

import br.com.zupacademy.propostas.cartao.bloqueio.ResponseApiBloqueio;
import br.com.zupacademy.propostas.cartao.viagem.ResponseApiAviso;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@FeignClient(url = "${feign.client.apiCartoes.url}", name = "apiCartoes")
public interface ApiCartoes {

    @PostMapping
    ResponsePostApiCartoes associarCartao(HashMap<String,String> dadosProposta);

    @PostMapping(value = "/{id}/avisos")
    ResponseApiAviso avisarSistemaLegado(@RequestBody Map<String,String> corpo, @PathVariable String id);

    @PostMapping(value = "/{id}/bloqueios")
     ResponseApiBloqueio bloquearCartao(@PathVariable String id, @RequestBody Map<String,String> corpo);
}
