package br.com.zupacademy.propostas.cartao.bloqueio;

import br.com.zupacademy.propostas.TestPrincipal;
import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.StatusCartao;
import br.com.zupacademy.propostas.proposta.Proposta;
import com.nimbusds.jwt.JWTParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtBearerTokenAuthenticationConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BloqueioControllerTest extends TestPrincipal {

    private final String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJySF9DODYxUHdBdlc5ZlVKeEtOelo0MW1SV1NFLXZRdGtBNTkzV09vLTB3In0.eyJleHAiOi0xNzk4NTQwODYwLCJpYXQiOjE2MzI1MTI4MzYsImp0aSI6IjJhYmM0MzA3LWNjOTUtNDM3My1hMDBjLWI2ODc0ZmM2OTdmYSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6MTgwODAvYXV0aC9yZWFsbXMvUHJvcG9zdGEiLCJzdWIiOiJjNWM4YzVhNS1mYzljLTRkMzYtYjRiNy0zNzEwMDMwODUxMTgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJQcm9wb3N0YSIsInNlc3Npb25fc3RhdGUiOiI2N2JiMDNmMy1mMDAzLTRmY2UtOWI1NC02YmMxOWI3MWZlOWYiLCJhY3IiOiIxIiwic2NvcGUiOiJUZXN0ZS1TY29wZSJ9.ihwrvkxLktCXNdXWZoFdJFZCOs5CF46lPbDAz3bAHGlOjqxofrjffwQwrpk508iUhAVOPKCSq2cIr6JLU7tDQquJjNsvz6mayQSIsafTwNq77nzIyGyOldreBhx5c84JCHhL9GkDPTdBKC1_mUOYm0jR8c-LQM6K0G3tRZsPZcG4KXHoDTkHKWZfepftIedz_xYj5F17LLqn8ehhlm1tOVI31QG2aGaDJ03VlUXcm9VtklXLNIkb-aGkFgACKxz0YPiPWmugGcrDRUUNOQWTJ6RCZtbZDuMOCoxmd_2Lh1r-muSJ5lDkJ5fogkSidjZlZu-CO0vbrgQ9BbYdqOacEw";

    @Test
    public void deveriaDarErroAoTentarBloquearCartaoQueNaoExiste() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cartoes/1234.1234/bloqueios"))

                .andExpect(MockMvcResultMatchers.status().isNotFound());
        Assertions.assertTrue(bloqueioCartaoRepository.findAll().isEmpty());
    }

    @Test
    public void deveriaCriarEntidadeDeBloqueioDoCartaoERetornarOk() throws Exception {
        Cartao cartao = super.criaCartaoSucesso("12345");
        cartaoRepository.save(cartao);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cartoes/"+cartao.getUuid()+"/bloqueios")
                       )
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assertions.assertTrue(!bloqueioCartaoRepository.findAll().isEmpty());
    }

    @Test
    public void deveriaDarErroAoTentarBloquearCartaoQueJáEstaBloqueado() throws Exception {
        Cartao cartao = super.criaCartaoSucesso("1234.1234");
        cartao.setStatus(StatusCartao.BLOQUEADO);
        cartaoRepository.save(cartao);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/cartoes/"+cartao.getUuid()+"/bloqueios")
                )
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
    }

}