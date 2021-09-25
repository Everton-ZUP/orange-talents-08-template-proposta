package br.com.zupacademy.propostas.cartao.bloqueio;

import br.com.zupacademy.propostas.TestPrincipal;
import br.com.zupacademy.propostas.cartao.ApiCartoes;
import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import br.com.zupacademy.propostas.cartao.StatusCartao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class AvisoSistemaLegadoBloqueioCartaoTest extends TestPrincipal {


    @Test
    public void bloquearCartaoComSucesso(){
        Cartao cartao = new Cartao("1234.1234.1234", LocalDateTime.now(),"Teste",null,
                new BigDecimal(1000),null,null,null,null,null);
        BloqueioCartao bloqueioCartao = new BloqueioCartao("123","123",cartao);
        cartaoRepository.save(cartao);
        bloqueioCartaoRepository.save(bloqueioCartao);
        cartao.setBloqueio(bloqueioCartao);
        cartaoRepository.save(cartao);

        ResponseApiBloqueio response = new ResponseApiBloqueio();
        response.setResultado("BLOQUEADO");

        Mockito.when(apiCartoes.bloquearCartao(Mockito.any(),Mockito.any())).thenReturn(response);
        avisoSistemaLegadoBloqueioCartao.avisarSistemaLegado();

        cartao = cartaoRepository.findById(cartao.getUuid()).get();
        Assertions.assertTrue(cartao.getStatus().equals(StatusCartao.BLOQUEADO),"Cartao deveria estar bloqueado");
    }

}