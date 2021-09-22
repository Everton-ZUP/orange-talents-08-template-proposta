package br.com.zupacademy.propostas.cartao.viagem;

import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class AvisoViagemController {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private AvisoViagemRepository avisoViagemRepository;

    @PostMapping("/cartoes/{id}/viagem")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public String adicionar(@PathVariable("id") String id, HttpServletRequest request, @RequestBody @Valid AvisoViagemRequest body){
        Cartao cartao = cartaoRepository.findById(id).orElseThrow(()->{
            System.out.println("cartão informado na url não encontrado");
            return new ResponseStatusException(HttpStatus.NOT_FOUND,"cartão não existe");
        });

        AvisoViagem viagem = body.toModel(request,cartao);
        avisoViagemRepository.save(viagem);

        return "viagem cadastrada com sucesso!";
    }
}
