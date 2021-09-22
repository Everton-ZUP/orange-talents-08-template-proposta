package br.com.zupacademy.propostas;

import br.com.zupacademy.propostas.cartao.ApiCartoes;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import br.com.zupacademy.propostas.cartao.bloqueio.ApiBloqueios;
import br.com.zupacademy.propostas.cartao.bloqueio.AvisoSistemaLegadoBloqueioCartao;
import br.com.zupacademy.propostas.cartao.bloqueio.BloqueioCartaoRepository;
import br.com.zupacademy.propostas.cartao.viagem.ApiAvisos;
import br.com.zupacademy.propostas.cartao.viagem.AvisoViagemRepository;
import br.com.zupacademy.propostas.proposta.PropostaRepository;
import br.com.zupacademy.propostas.proposta.avaliacao.ApiAvaliacaoFinanceira;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TestPrincipal {


    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected PropostaRepository propostaRepository;
    @Autowired
    protected CartaoRepository cartaoRepository;
    @Autowired
    protected BloqueioCartaoRepository bloqueioCartaoRepository;
    @Autowired
    protected AvisoViagemRepository avisoViagemRepository;


    @MockBean
    protected ApiAvaliacaoFinanceira apiAvaliacaoFinanceira;
    @MockBean
    protected ApiCartoes apiCartoes;
    @MockBean
    protected AvisoSistemaLegadoBloqueioCartao avisoSistemaLegadoBloqueioCartao;
    @MockBean
    protected ApiBloqueios apiBloqueios;
    @MockBean
    protected ApiAvisos apiAvisos;

    @Mock
    protected Jwt jwt;
}
