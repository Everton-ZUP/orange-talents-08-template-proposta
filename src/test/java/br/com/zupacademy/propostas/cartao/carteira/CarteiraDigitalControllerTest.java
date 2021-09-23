package br.com.zupacademy.propostas.cartao.carteira;

import br.com.zupacademy.propostas.TestPrincipal;
import br.com.zupacademy.propostas.cartao.Cartao;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

class CarteiraDigitalControllerTest extends TestPrincipal {

    @Test
    public void deveriaVincularComSucessoAsCarteirasDigitaisExistentes() throws Exception {
        Cartao cartao = super.criaCartaoSucesso("9087.8907.1234.4321");

        ResponseApiCarteira responseApiCarteira = new ResponseApiCarteira("ASSOCIADA","adajuweh1iu23");
        Mockito.when(apiCartoes.adicionarCarteira(Mockito.any(),Mockito.any())).thenReturn(responseApiCarteira);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/"+cartao.getUuid()+"/carteiras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                            Map.of("email","teste@teste.com","carteira",TipoCarteira.PAYPAL.name()))
                        ))
                .andDo(System.out::println)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/cartoes/"+cartao.getUuid()+"/carteiras/*"));
    }

    @Test
    public void deveriaDarErroAoTentarCadastrarCarteiraEmCartaoQueNaoExiste() throws Exception {

        ResponseApiCarteira responseApiCarteira = new ResponseApiCarteira("ASSOCIADA","adajuweh1iu23");
        Mockito.when(apiCartoes.adicionarCarteira(Mockito.any(),Mockito.any())).thenReturn(responseApiCarteira);

        mockMvc.perform(MockMvcRequestBuilders.post("/cartoes/1231231231232222/carteiras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of("email","teste@teste.com","carteira",TipoCarteira.PAYPAL.name()))
                        ))
                .andDo(System.out::println)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deveriaDarErroAoTentarCadastrarDuasVezesOCartaoNaMesmaCarteira() throws Exception {
        Cartao cartao = super.criaCartaoSucesso("1234.8907.1234.4321");
        cartao.adicionarCarteira(new CarteiraDigital("123123","teste@Teste.com",cartao,TipoCarteira.PAYPAL));
        cartaoRepository.save(cartao);

        ResponseApiCarteira responseApiCarteira = new ResponseApiCarteira("ASSOCIADA","adajuweh1iu23");
        Mockito.when(apiCartoes.adicionarCarteira(Mockito.any(),Mockito.any())).thenReturn(responseApiCarteira);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/cartoes/" + cartao.getUuid() + "/carteiras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        Map.of("email", "teste@teste.com", "carteira", TipoCarteira.PAYPAL.name()))
                );
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

    }

    @Test
    public void deveriaDarErroQuandoARespostaDaApiExternaVoltarComFalha() throws Exception {
        Cartao cartao = super.criaCartaoSucesso("1234.8907.1234.4321");

        Mockito.when(apiCartoes.adicionarCarteira(Mockito.any(),Mockito.any())).thenThrow(FeignException.class);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cartoes/" + cartao.getUuid() + "/carteiras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                Map.of("email", "teste@teste.com", "carteira", TipoCarteira.PAYPAL.name()))
                ))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

    }
}