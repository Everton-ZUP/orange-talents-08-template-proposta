package br.com.zupacademy.propostas.proposta.cadastro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("Test")
class PropostaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void cadastrarPropostaComSucesso() throws Exception {
        PropostaRequest request = new PropostaRequest("298.625.190-02",
                "teste@zup.com","Teste","Rua do Teste", new BigDecimal(1000));

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                    .content(new ObjectMapper().writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/propostas/*"));
    }

    @ParameterizedTest
    @CsvSource({",teste@zup.com,teste,Rua Teste",
            "298.625.190-02,,teste,Rua Teste",
            "298.625.190-02,teste@zup.com,'',Rua Teste",
            "298.625.190-02,teste@zup.com,teste,"})
    void deveriaDarErroAoInserirPropostaComAlgumCampoVazio(String documento,String email, String nome, String endereco ) throws Exception {
        PropostaRequest request = new PropostaRequest(documento,email,nome,endereco, new BigDecimal(1000));
        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({"0,false","1,true","-10,false"})
    void deveriaDarErroAoInserirPropostaComSalarioNegativo(BigDecimal salario, Boolean resultado) throws Exception {
        PropostaRequest request = new PropostaRequest("298.625.190-02",
                "teste@zup.com","Teste","Rua do Teste", salario);

        RequestBuilder builder = MockMvcRequestBuilders.post("/propostas")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON);

        if (resultado) {
            mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isCreated());
        }else{
            mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }
}