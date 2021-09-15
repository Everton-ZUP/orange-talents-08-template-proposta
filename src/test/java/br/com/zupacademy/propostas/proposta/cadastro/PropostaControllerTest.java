package br.com.zupacademy.propostas.proposta.cadastro;

import br.com.zupacademy.propostas.cartao.ApiCartoes;
import br.com.zupacademy.propostas.proposta.EstadoProposta;
import br.com.zupacademy.propostas.proposta.PropostaRepository;
import br.com.zupacademy.propostas.proposta.avaliacao.ApiAvaliacaoFinanceira;
import br.com.zupacademy.propostas.proposta.avaliacao.EnumAvaliacaoFinanceiraResultado;
import br.com.zupacademy.propostas.proposta.avaliacao.ResponseAvaliacaoFinanceira;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("Test")
class PropostaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PropostaRepository propostaRepository;

    @MockBean
    private ApiAvaliacaoFinanceira apiAvaliacaoFinanceira;

    @MockBean
    private ApiCartoes apiCartoes;


    @Test
    void cadastrarPropostaComSucesso() throws Exception {
        PropostaRequest request = new PropostaRequest("298.625.190-02",
                "teste@zup.com","Teste","Rua do Teste", new BigDecimal(1000));

        ResponseAvaliacaoFinanceira responseAvaliacaoFinanceira = new ResponseAvaliacaoFinanceira();
        responseAvaliacaoFinanceira.setResultadoSolicitacao(EnumAvaliacaoFinanceiraResultado.SEM_RESTRICAO);
        Mockito.when(apiAvaliacaoFinanceira.fazerAvaliacaoFinaceira(Mockito.any())).thenReturn(responseAvaliacaoFinanceira);

        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                    .content(new ObjectMapper().writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/propostas/*"));

        Assertions.assertTrue(propostaRepository.findByDocumento("298.625.190-02").getEstado().equals(EstadoProposta.ELEGIVEL));
    }

    @ParameterizedTest
    @CsvSource({",teste@zup.com,teste,Rua Teste",
            "298.625.190-02,,teste,Rua Teste",
            "298.625.190-02,teste@zup.com,'',Rua Teste",
            "298.625.190-02,teste@zup.com,teste,"})
    void deveriaDarErroAoInserirPropostaComAlgumCampoVazio(String documento,String email, String nome, String endereco ) throws Exception {
        PropostaRequest request = new PropostaRequest(documento,email,nome,endereco, new BigDecimal(1000));

        ResponseAvaliacaoFinanceira responseAvaliacaoFinanceira = new ResponseAvaliacaoFinanceira();
        responseAvaliacaoFinanceira.setResultadoSolicitacao(EnumAvaliacaoFinanceiraResultado.SEM_RESTRICAO);
        Mockito.when(apiAvaliacaoFinanceira.fazerAvaliacaoFinaceira(Mockito.any())).thenReturn(responseAvaliacaoFinanceira);

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

        ResponseAvaliacaoFinanceira responseAvaliacaoFinanceira = new ResponseAvaliacaoFinanceira();
        responseAvaliacaoFinanceira.setResultadoSolicitacao(EnumAvaliacaoFinanceiraResultado.SEM_RESTRICAO);
        Mockito.when(apiAvaliacaoFinanceira.fazerAvaliacaoFinaceira(Mockito.any())).thenReturn(responseAvaliacaoFinanceira);

        RequestBuilder builder = MockMvcRequestBuilders.post("/propostas")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON);

        if (resultado) {
            mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isCreated());
        }else{
            mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }

    @Test
    void deveriaDarErroAoCadastrarPropostaComDocumentoDuplicado() throws Exception {
        PropostaRequest request = new PropostaRequest("298.625.190-02",
                "teste@zup.com","Teste","Rua do Teste", new BigDecimal(1000));

        ResponseAvaliacaoFinanceira responseAvaliacaoFinanceira = new ResponseAvaliacaoFinanceira();
        responseAvaliacaoFinanceira.setResultadoSolicitacao(EnumAvaliacaoFinanceiraResultado.SEM_RESTRICAO);
        Mockito.when(apiAvaliacaoFinanceira.fazerAvaliacaoFinaceira(Mockito.any())).thenReturn(responseAvaliacaoFinanceira);

        RequestBuilder builder = MockMvcRequestBuilders.post("/propostas")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isCreated());
        mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

    }

    @Test
    void deveriaSalvarAPropostaComoNaoElegivel() throws Exception {
        PropostaRequest request = new PropostaRequest("298.625.190-02",
                "teste@zup.com","Teste","Rua do Teste", new BigDecimal(1000));

        ResponseAvaliacaoFinanceira responseAvaliacaoFinanceira = new ResponseAvaliacaoFinanceira();
        responseAvaliacaoFinanceira.setResultadoSolicitacao(EnumAvaliacaoFinanceiraResultado.COM_RESTRICAO);

        Mockito.doThrow(
                new FeignException.UnprocessableEntity("",
                        Request.create(Request.HttpMethod.POST,"vazia",Map.of("", List.of("")),
                                new ObjectMapper().writeValueAsBytes(""),null,null),
                        new ObjectMapper().writeValueAsString(responseAvaliacaoFinanceira).getBytes(StandardCharsets.UTF_8)))
                        .when(apiAvaliacaoFinanceira).fazerAvaliacaoFinaceira(Mockito.any());


        mockMvc.perform(MockMvcRequestBuilders.post("/propostas")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/propostas/*"));

        Assertions.assertTrue(propostaRepository.findByDocumento("298.625.190-02").getEstado().equals(EstadoProposta.NAO_ELEGIVEL));
    }

    @Test
    void retornarDadosAoBuscarEErroAoBuscarUmIdInvalido() throws Exception {
        PropostaRequest request = new PropostaRequest("298.625.190-02",
                "teste@zup.com","Teste","Rua do Teste", new BigDecimal(1000));

        ResponseAvaliacaoFinanceira responseAvaliacaoFinanceira = new ResponseAvaliacaoFinanceira();
        responseAvaliacaoFinanceira.setResultadoSolicitacao(EnumAvaliacaoFinanceiraResultado.SEM_RESTRICAO);
        Mockito.when(apiAvaliacaoFinanceira.fazerAvaliacaoFinaceira(Mockito.any())).thenReturn(responseAvaliacaoFinanceira);

        String location = mockMvc.perform( MockMvcRequestBuilders.post("/propostas")
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getHeader("Location");


        mockMvc.perform(MockMvcRequestBuilders.get(location))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("documento").value("298.625.190-02"));

        mockMvc.perform(MockMvcRequestBuilders.get("/propostas/10000"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}