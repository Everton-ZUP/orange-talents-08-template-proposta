package br.com.zupacademy.propostas.cartao.bloqueio;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(url = "${feign.client.apiCartoes.url}", name = "apiBloqueio")
public interface ApiBloqueios {

    @PostMapping(value = "/{id}/bloqueios")
    ResponseApiBloqueio bloquearCartao(@PathVariable String id, @RequestBody Map<String,String> corpo);

}