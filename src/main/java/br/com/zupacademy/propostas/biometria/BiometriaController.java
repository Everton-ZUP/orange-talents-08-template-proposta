package br.com.zupacademy.propostas.biometria;

import br.com.zupacademy.propostas.biometria.cadastro.RequestBiometria;
import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class BiometriaController {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private BiometriaRepository biometriaRepository;

    @PostMapping
    @RequestMapping("/cartoes/{id}/biometrias")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> adicionarBiometria(@PathVariable("id") String nCartao,
                                                @RequestBody @Valid RequestBiometria biometria, UriComponentsBuilder uri){

        Cartao cartao = cartaoRepository.findById(nCartao).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"cartão não exite")
        );

        biometria.validarBiometria();

        Biometria bio = biometria.toBiometria(cartao);
        biometriaRepository.save(bio);

        cartao.adicionarBiometria(bio);
        cartaoRepository.save(cartao);

        return ResponseEntity.created(uri.path("/cartoes/{id}/biometrias/{biometria}")
                .build(cartao.getNumeroCartao(),bio.getUuid())).build();

    }

}
