package br.com.zupacademy.propostas.cartao.viagem;

import br.com.zupacademy.propostas.TestPrincipal;
import br.com.zupacademy.propostas.cartao.Cartao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AvisoViagemControllerTest extends TestPrincipal {

    @Test
    public void deveriaCadastrarAvisoDeViagemComSucesso() throws Exception {
        Cartao cartao = new Cartao("9087.8907.1234.4321", LocalDateTime.now(),"Teste",null,
                new BigDecimal(1000),null,null,null,null,null,null);
        cartaoRepository.save(cartao);
        ResponseApiAviso responseApiAviso = new ResponseApiAviso(AvisoViagemRetornoEnum.CRIADO);
        Mockito.when(apiAvisos.avisarSistemaLegado(Mockito.any(),Mockito.any())).thenReturn(responseApiAviso);

        HashMap<String,String> request = new HashMap<>();
        request.put("destinoViagem","cascavel-PR");
        request.put("terminoViagem","2021-11-21");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/"+cartao.getUuid()+"/viagem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void deveriaDarErroAoCadastrarAvisoDeViagemEmUmCartaoQueNaoExiste() throws Exception {
        HashMap<String,String> request = new HashMap<>();
        request.put("destinoViagem","cascavel-PR");
        request.put("terminoViagem","2021-11-21");
        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/1234/viagem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @CsvSource({"3021-12-01","2020-12-01"})
    public void deveriaDarErroAoCadastrarAvisoDeViagemFaltandoCampoDataTerminoDaViagemOuNoPassado(LocalDate data) throws Exception {
        Cartao cartao = new Cartao("9087.8907.1234.4321", LocalDateTime.now(),"Teste",null,
                new BigDecimal(1000),null,null,null,null,null,null);
        cartaoRepository.save(cartao);
        ResponseApiAviso responseApiAviso = new ResponseApiAviso(AvisoViagemRetornoEnum.CRIADO);
        Mockito.when(apiAvisos.avisarSistemaLegado(Mockito.any(),Mockito.any())).thenReturn(responseApiAviso);

        HashMap<String,String> request = new HashMap<>();
        request.put("destinoViagem","cascavel-PR");
        request.put("terminoViagem",data.toString());

        ResultActions teste = mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getUuid() + "/viagem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)));

        if (data.isAfter(LocalDate.now())){
            teste.andExpect(MockMvcResultMatchers.status().isOk());
        }else{
            teste.andExpect(MockMvcResultMatchers.status().isBadRequest());
        }
    }
    @Test
    public void deveriaDarErroAoCadastrarAvisoDeViagemFaltandoCampoDestino() throws Exception {
        Cartao cartao = new Cartao("9087.8907.1234.4321", LocalDateTime.now(),"Teste",null,
                new BigDecimal(1000),null,null,null,null,null,null);
        cartaoRepository.save(cartao);

        HashMap<String,String> request = new HashMap<>();
        request.put("destinoViagem","");
        request.put("terminoViagem","4210-12-12");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getUuid() + "/viagem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void deveriaDarErroAoCadastrarAvisoDeViagemQueRetornaComFalhaDaApiExterna() throws Exception {
        Cartao cartao = new Cartao("9087.8907.1234.4321", LocalDateTime.now(),"Teste",null,
                new BigDecimal(1000),null,null,null,null,null,null);
        cartaoRepository.save(cartao);
        ResponseApiAviso responseApiAviso = new ResponseApiAviso(AvisoViagemRetornoEnum.FALHA);
        Mockito.when(apiAvisos.avisarSistemaLegado(Mockito.any(),Mockito.any())).thenReturn(responseApiAviso);

        HashMap<String,String> request = new HashMap<>();
        request.put("destinoViagem","cascavel-PR");
        request.put("terminoViagem","4000-01-01");

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/" + cartao.getUuid() + "/viagem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

    }
}