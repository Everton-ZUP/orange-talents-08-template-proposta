package br.com.zupacademy.propostas.cartao;

import br.com.zupacademy.propostas.proposta.EstadoProposta;
import br.com.zupacademy.propostas.proposta.Proposta;
import br.com.zupacademy.propostas.proposta.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssociarCartao {

    @Autowired
    private PropostaRepository propostaRepository;

    @Scheduled(fixedRateString = "${associar.cartao.tempo.fixo}")
    private void tentativaDeAssociarCartoes(){
        List<Proposta> listaPropostas = propostaRepository.findAllByStatusAndCartaoIsNull(EstadoProposta.ELEGIVEL);

        listaPropostas.forEach(proposta -> {
            //conectar na api
        });
    }

}
