package br.com.zupacademy.propostas.cartao;

import br.com.zupacademy.propostas.proposta.EstadoProposta;
import br.com.zupacademy.propostas.proposta.Proposta;
import br.com.zupacademy.propostas.proposta.PropostaRepository;
import feign.FeignException;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Component
public class AssociarCartao {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private ApiCartoes apiCartoes;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MeterRegistry registry;

    @Scheduled(fixedRateString = "${associar.cartao.tempo.fixo}")
    private void tentativaDeAssociarCartoes(){

        List<Proposta> listaPropostas = propostaRepository.findAllByEstadoAndCartaoIsNull(EstadoProposta.ELEGIVEL);

        listaPropostas.forEach(proposta -> {
            registry.timer("tempo_associar_cartao_proposta").record(()->{
                HashMap<String,String> dadosRequisicao = new HashMap<>();
                dadosRequisicao.put("documento",proposta.getDocumento());
                dadosRequisicao.put("nome",proposta.getNome());
                dadosRequisicao.put("idProposta",proposta.getId().toString());

                try{
                    ResponsePostApiCartoes respApiCartoes = apiCartoes.associarCartao(dadosRequisicao);
                    Cartao cartao = respApiCartoes.toCartao();
                    proposta.setCartao(cartao);
                    propostaRepository.save(proposta);
                    logger.info("Cartão associado com sucesso para proposta "+proposta.getId().toString());
                    registry.counter("associar_cartao_proposta_sucesso").increment();
                }catch (FeignException exception){
                    logger.error("Erro na resposta da api de cartões idProposta:"+proposta.getId().toString());
                    registry.counter("associar_cartao_proposta_falha").increment();
                }
            });

        });
    }

}
