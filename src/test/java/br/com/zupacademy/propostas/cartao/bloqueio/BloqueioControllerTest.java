package br.com.zupacademy.propostas.cartao.bloqueio;

import br.com.zupacademy.propostas.TestPrincipal;
import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.proposta.Proposta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BloqueioControllerTest extends TestPrincipal {

    @Test
    public void deveriaCriarEntidadeDeBloqueioDoCartaoERetornarOk() throws Exception {
        Proposta proposta = new Proposta("169.059.230-36","zup@zup.test","Teste","null",new BigDecimal(100));
        Cartao cartao = new Cartao("1234.1234", LocalDateTime.now(),"Teste",proposta,new BigDecimal(100),null,null,null,null,null,null);
        proposta.setCartao(cartao);
        cartaoRepository.save(cartao);
        propostaRepository.save(proposta);


//       mockMvc.perform(MockMvcRequestBuilders
//                        .post("/cartoes/1234.1234/bloqueios"))
//
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        Assertions.assertTrue(!bloqueioCartaoRepository.findAll().isEmpty());

    }

}