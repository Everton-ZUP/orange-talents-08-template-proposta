package br.com.zupacademy.propostas.cartao.bloqueio;

import br.com.zupacademy.propostas.cartao.ApiCartoes;
import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import br.com.zupacademy.propostas.cartao.StatusCartao;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AvisoSistemaLegadoBloqueioCartao {

    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private ApiCartoes apiBloqueios;

    @Scheduled(fixedRateString = "${bloquear.cartao.tempo.fixo}")
    public void avisarSistemaLegado(){
        List<Cartao> cartoesParaSeremAtualizados = cartaoRepository.buscaCartoesBloqueadosComStatusDesatualizado(StatusCartao.BLOQUEADO);

        if (!cartoesParaSeremAtualizados.isEmpty()){
            for (Cartao cartao : cartoesParaSeremAtualizados){
                try {

                    ResponseApiBloqueio response = apiBloqueios.bloquearCartao(cartao.getNumeroCartao(),
                            Map.of("sistemaResponsavel","proposta"));
                    if (response.getResultado().equals("BLOQUEADO")){
                        cartao.setStatus(StatusCartao.BLOQUEADO);
                        cartaoRepository.save(cartao);
                        System.out.println(cartao.getNumeroCartao()+" bloqueado");
                    }
                }catch (FeignException err){
                    System.out.println("Erro ao informar API para bloqueio de cartão"+err.toString());
                }
            }
        }
    }

}
