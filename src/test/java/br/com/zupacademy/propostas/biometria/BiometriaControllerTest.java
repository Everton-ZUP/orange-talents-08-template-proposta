package br.com.zupacademy.propostas.biometria;

import br.com.zupacademy.propostas.TestPrincipal;
import br.com.zupacademy.propostas.cartao.Cartao;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BiometriaControllerTest extends TestPrincipal {

    @Test
    public void deveriaCadastrarBiometriaComSucesso() throws Exception {
        Cartao cartao = new Cartao("9087.8907.1234.4321", LocalDateTime.now(),"Teste",null,
                new BigDecimal(1000),null,null,null,null,null,null);

        cartaoRepository.save(cartao);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/"+cartao.getUuid()+"/biometrias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fingerPrint\":\"MTIz\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/cartoes/9087.8907.1234.4321/biometrias/**"));
    }

    @Test
    public void deveriaDarErroAoCadastrarFingerPrintSemSerEmBase64() throws Exception {
        Cartao cartao = new Cartao("9087.8907.1234.4322", LocalDateTime.now(),"Teste",null,
                new BigDecimal(1000),null,null,null,null,null,null);
        cartao = cartaoRepository.save(cartao);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/"+cartao.getUuid()+"/biometrias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fingerPrint\":\"123\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void deveriaDarErroAoCadastrarFingerPrintEmUmCartaoQueNaoExiste() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/j89ysadfsydfsafsdf/biometrias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fingerPrint\":\"MTIz\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}