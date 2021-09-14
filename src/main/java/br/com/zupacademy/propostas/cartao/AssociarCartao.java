package br.com.zupacademy.propostas.cartao;

import br.com.zupacademy.propostas.proposta.EstadoProposta;
import br.com.zupacademy.propostas.proposta.Proposta;
import br.com.zupacademy.propostas.proposta.PropostaRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AssociarCartao {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private ApiCartoes apiCartoes;

    @Scheduled(fixedRateString = "${associar.cartao.tempo.fixo}")
    private void tentativaDeAssociarCartoes(){
        List<Proposta> listaPropostas = propostaRepository.findAllByEstadoAndCartaoIsNull(EstadoProposta.ELEGIVEL);

        listaPropostas.forEach(proposta -> {
            HashMap<String,String> dadosRequisicao = new HashMap<>();
            dadosRequisicao.put("documento",proposta.getDocumento());
            dadosRequisicao.put("nome",proposta.getNome());
            dadosRequisicao.put("idProposta",proposta.getId().toString());

            try{
                ResponsePostApiCartoes respApiCartoes = apiCartoes.associarCartao(dadosRequisicao);
                Cartao cartao = respApiCartoes.toCartao();
                proposta.setCartao(cartao);
                propostaRepository.save(proposta);
            }catch (FeignException exception){
                System.out.println("Erro na resposta da api de cartões idProposta:"+proposta.getId().toString());
            }

        });
    }

}
