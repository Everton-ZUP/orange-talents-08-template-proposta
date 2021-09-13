package br.com.zupacademy.propostas.proposta.avaliacao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@FeignClient(url = "${feign.client.apiAvaliacaoFinanceira.url}", name = "apiAvaliacaoFinanceira")
public interface ApiAvaliacaoFinanceira {

    @PostMapping
    String fazerAvaliacaoFinaceira(@RequestBody HashMap<String,String> request);

}
