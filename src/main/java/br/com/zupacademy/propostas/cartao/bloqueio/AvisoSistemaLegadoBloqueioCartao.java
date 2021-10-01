package br.com.zupacademy.propostas.cartao.bloqueio;

import br.com.zupacademy.propostas.cartao.ApiCartoes;
import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import br.com.zupacademy.propostas.cartao.StatusCartao;
import feign.FeignException;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

@Component
public class AvisoSistemaLegadoBloqueioCartao {

    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private ApiCartoes apiBloqueios;

    @Autowired
    private MeterRegistry meterRegistry;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(fixedDelayString = "${bloquear.cartao.tempo.fixo}")
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

                        meterRegistry.counter("cartoes_bloqueados_sucesso").increment();
                        logger.info(cartao.getNumeroCartao()+" bloqueado");
                    }
                }catch (FeignException err){
                    meterRegistry.counter("cartoes_bloqueados_falha").increment();
                    logger.error("Erro ao informar API para bloqueio de cartão"+err.toString());
                }
            }
        }
    }

}
