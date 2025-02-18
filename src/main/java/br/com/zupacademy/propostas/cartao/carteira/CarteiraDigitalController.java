package br.com.zupacademy.propostas.cartao.carteira;

import br.com.zupacademy.propostas.cartao.ApiCartoes;
import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import br.com.zupacademy.propostas.seguranca.DadosSensiveisOfuscar;
import feign.FeignException;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentracing.Span;
import io.opentracing.Tracer;
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
    private MeterRegistry registry;

    @Autowired
    private CarteiraDigitalRepository carteiraDigitalRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private ApiCartoes apiCartoes;

    @Autowired
    private Tracer tracer;

    @PostMapping("/cartoes/{id}/carteiras")
    private ResponseEntity<?> adicionarCarteira(@PathVariable("id") String id,
                                                @RequestBody @Valid AdicionarCarteiraRequest request,
                                                UriComponentsBuilder uri){
        Span span = tracer.activeSpan();
        span.setTag("usuario.email", request.getEmail());

        Cartao cartao = cartaoRepository.findById(id).orElseThrow(()->{
            logger.warn("Não foi possível encontrar cartão com o id "+id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND,"Cartão informado não encontrado");
        });

        if (cartao.estaAssociadoACarteira(request.getCarteira())){
            logger.warn("Não foi possível cadastrar o cartão na carteira solicitada, pois ele já está associado ");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Não foi possível associar o cartão a carteira, pois ele já está associado a mesma");
        }

        try {
            ResponseApiCarteira responseApiCarteira = apiCartoes.adicionarCarteira(cartao.getNumeroCartao(),
                    request.corpoAvisoSistemaLegado());
            CarteiraDigital carteira = request.toModel(responseApiCarteira.getId(), cartao);
            carteiraDigitalRepository.save(carteira);

            logger.info("Carteira: "+carteira.getId()+"associada com sucesso para o cartão: "
                    + DadosSensiveisOfuscar.ofuscar(cartao.getNumeroCartao()));
            registry.counter("Associar_Cartao_Carteira_sucesso").increment();
            return ResponseEntity.created(uri.path("/cartoes/{id}/carteiras/{idc}").build(cartao.getUuid(),carteira.getId())).build();

        }catch (FeignException error){
            registry.counter("Associar_Cartao_Carteira_falha").increment();
            logger.error("Não foi possível cadastrar o cartão na carteira solicitada "+error.toString());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Não foi possível associar o cartão a carteira");
        }
    }
}
