package br.com.zupacademy.propostas.cartao.carteira;

import br.com.zupacademy.propostas.cartao.ApiCartoes;
import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
public class CarteiraDigitalController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CarteiraDigitalRepository carteiraDigitalRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private ApiCartoes apiCartoes;

    @PostMapping("/cartoes/{id}/carteiras")
    private ResponseEntity<?> adicionarCarteira(@PathVariable("id") String id,
                                                @RequestBody @Valid AdicionarCarteiraRequest request,
                                                UriComponentsBuilder uri){

        Cartao cartao = cartaoRepository.findById(id).orElseThrow(()->{
            logger.error("Não foi possível encontrar cartão com o id "+id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND,"Cartão informado não encontrado");
        });

        if (cartao.estaAssociadoACarteira(request.getCarteira())){
            logger.error("Não foi possível cadastrar o cartão na carteira solicitada, pois ele já está associado ");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Não foi possível associar o cartão a carteira, pois ele já está associado a mesma");
        }

        try {
            ResponseApiCarteira responseApiCarteira = apiCartoes.adicionarCarteira(cartao.getNumeroCartao(),
                    request.corpoAvisoSistemaLegado());
            CarteiraDigital carteira = request.toModel(responseApiCarteira.getId(), cartao);
            carteiraDigitalRepository.save(carteira);

            logger.info("Carteira: "+carteira.getId()+"associada com sucesso para o cartão: ****.****."
                    +cartao.getNumeroCartao().substring(13));
            return ResponseEntity.created(uri.path("/cartoes/{id}/carteiras/{idc}").build(cartao.getUuid(),carteira.getId())).build();

        }catch (FeignException error){
            logger.error("Não foi possível cadastrar o cartão na carteira solicitada "+error.toString());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Não foi possível associar o cartão a carteira");
        }
    }
}
