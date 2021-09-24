package br.com.zupacademy.propostas.cartao.bloqueio;

import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import br.com.zupacademy.propostas.exception.ErroRegraDeNegocio;
import br.com.zupacademy.propostas.seguranca.DadosSensiveisOfuscar;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RestController
public class BloqueioController {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BloqueioCartaoRepository bloqueioCartaoRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MeterRegistry registry;

    @PostMapping   @ResponseStatus(HttpStatus.OK)  @Transactional
    @RequestMapping("/cartoes/{id}/bloqueios")
    public String bloquearCartao(@PathVariable("id") String numCartao, HttpServletRequest request, @AuthenticationPrincipal Jwt token){

        Cartao cartao = cartaoRepository.findById(numCartao).orElseThrow(()->{
                    logger.warn("cartão informado na url não encontrado");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,"cartão não existe");
                }
        );

        if (!cartao.getProposta().getDocumento().equals(token.getClaims().get("documento"))){
            logger.warn("Documento não pertence ao usuario que solicitou "+cartao.getProposta().getDocumento()+" != "+token.getClaims().get("documento"));
            throw new ErroRegraDeNegocio("Este cartão não pertence ao usuário logado","cartão", numCartao);
        }

        if (cartao.cartaoBloqueado()){
            logger.warn("Cartão "+ DadosSensiveisOfuscar.ofuscar(cartao.getNumeroCartao())+"já está bloqueado");
            throw new ErroRegraDeNegocio("Este cartão já está bloqueado","cartão", numCartao);
        }

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String userAgent = request.getHeader("User-Agent");

        BloqueioCartao bloqueioCartao = new BloqueioCartao(ipAddress,userAgent,cartao);
        registry.counter("bloqueio_cartao").increment();
        bloqueioCartaoRepository.save(bloqueioCartao);
        logger.info("criado registro de cloqueio para o cartão "+DadosSensiveisOfuscar.ofuscar(cartao.getNumeroCartao()));
        return "Criado solicitação para bloqueio de cartão";
    }

}
