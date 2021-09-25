package br.com.zupacademy.propostas.cartao;

import br.com.zupacademy.propostas.TestPrincipal;
import br.com.zupacademy.propostas.proposta.EstadoProposta;
import br.com.zupacademy.propostas.proposta.Proposta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

class AssociarCartaoTest extends TestPrincipal {

    @Autowired
    protected AssociarCartao associarCartaoClasse;

    @Test
    public void deveriaVincularCartaoComSucesso(){
        Proposta proposta = new Proposta("123.123","teste@tete.c",
            "teste","vazio",new BigDecimal(100));
        proposta.setEstado(EstadoProposta.ELEGIVEL);
        propostaRepository.save(proposta);

        ResponsePostApiCartoes response = new ResponsePostApiCartoes("123123123", LocalDateTime.now(),
                "123","123",new BigDecimal(10000),null,null,null,null,null, Map.of());

        Mockito.when(apiCartoes.associarCartao(Mockito.any())).thenReturn(response);

        associarCartaoClasse.tentativaDeAssociarCartoes();

        Assertions.assertNotNull(propostaRepository.findById(proposta.getId()).get().getCartao());
    }


}