package br.com.zupacademy.propostas.cartao.bloqueio;

import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import br.com.zupacademy.propostas.exception.ErroRegraDeNegocio;
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

    @PostMapping   @ResponseStatus(HttpStatus.OK)  @Transactional
    @RequestMapping("/cartoes/{id}/bloqueios")
    public String bloquearCartao(@PathVariable("id") String numCartao, HttpServletRequest request, @AuthenticationPrincipal Jwt token){

        Cartao cartao = cartaoRepository.findById(numCartao).orElseThrow(()->{
                    System.out.println("cartão informado na url não encontrado");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,"cartão não existe");
                }
        );

        if (!cartao.getProposta().getDocumento().equals(token.getClaims().get("documento"))){
            System.out.println(cartao.getProposta().getDocumento()+" != "+token.getClaims().get("documento"));
            throw new ErroRegraDeNegocio("Este cartão não pertence ao usuário logado","cartão", numCartao);
        }

        if (cartao.cartaoBloqueado()){
            throw new ErroRegraDeNegocio("Este cartão já está bloqueado","cartão", numCartao);
        }

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String userAgent = request.getHeader("User-Agent");

        BloqueioCartao bloqueioCartao = new BloqueioCartao(ipAddress,userAgent,cartao);

        bloqueioCartaoRepository.save(bloqueioCartao);

        return "Cartão bloqueado com sucesso";
    }

}
