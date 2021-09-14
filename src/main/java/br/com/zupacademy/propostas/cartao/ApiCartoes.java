package br.com.zupacademy.propostas.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;

@FeignClient(url = "${feign.client.apiCartoes.url}", name = "apiCartoes")
public interface ApiCartoes {

    @PostMapping
    public ResponsePostApiCartoes associarCartao(HashMap<String,String> dadosProposta);

}
