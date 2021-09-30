package br.com.zupacademy.propostas.proposta;

import br.com.zupacademy.propostas.proposta.avaliacao.ApiAvaliacaoFinanceira;
import br.com.zupacademy.propostas.proposta.avaliacao.ResponseAvaliacaoFinanceira;
import br.com.zupacademy.propostas.proposta.cadastro.PropostaRequest;
import br.com.zupacademy.propostas.proposta.consulta.PropostaConsultaResponse;
import br.com.zupacademy.propostas.seguranca.DadosSensiveisOfuscar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private ApiAvaliacaoFinanceira apiAvaliacaoFinanceira;

    @Autowired
    private MeterRegistry meterRegistry;

    private final Tracer tracer;
    public PropostaController(Tracer tracer) {
        this.tracer = tracer;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/{id}") @ResponseStatus(HttpStatus.OK)
    public PropostaConsultaResponse buscarProposta(@PathVariable("id") Long idProposta){

        Proposta proposta =
                this.meterRegistry.timer("tempo-busca-proposta").record(()->{
                    return propostaRepository.findById(idProposta).orElseThrow(() -> {
                                logger.error("Falha ao encontrar a proposta "+idProposta);
                                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Proposta não encontrada");
                            });
        });
        return new PropostaConsultaResponse(proposta);
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarProposta(@RequestBody @Valid PropostaRequest formulario, UriComponentsBuilder uri) {
        Span span = tracer.activeSpan();
        span.setBaggageItem("user.email", formulario.getEmail());
        span.setTag("usuario.email", formulario.getEmail());
        return this.meterRegistry.timer("tempo-criar-proposta").record(()->{
                Proposta proposta = formulario.toModel();

                proposta = propostaRepository.save(proposta);

                ResponseAvaliacaoFinanceira retorno = getRespostaAvaliacaoFinanceira(proposta);

                proposta.setEstado(retorno.getStatusProposta());
                    proposta = propostaRepository.save(proposta);
                    meterRegistry.counter("propostas-criadas").increment();
                    URI location = uri.path("/propostas/{id}").build(proposta.getId());
                    logger.info("Proposta Criada com sucesso! "+DadosSensiveisOfuscar.ofuscar(proposta.getDocumento()));
                    return ResponseEntity.created(location).build();
        });
    }

    private ResponseAvaliacaoFinanceira getRespostaAvaliacaoFinanceira(Proposta proposta) {
        HashMap<String,String> formApi = new HashMap<>();
        formApi.put("documento", proposta.getDocumento());
        formApi.put("nome", proposta.getNome());
        formApi.put("idProposta", proposta.getId().toString());

        ResponseAvaliacaoFinanceira retorno = null;
        try {
            retorno = apiAvaliacaoFinanceira.fazerAvaliacaoFinaceira(formApi);
            meterRegistry.counter("propostas-criadas-elegiveis").increment();
        }catch (FeignException feignException){
            if (feignException.contentUTF8().isEmpty()){
                logger.error("Resposta da API de avaliação financeira retornou com erro"
                        +feignException.getCause().toString());
            }else{
                try {
                    retorno = new ObjectMapper().readValue(feignException.contentUTF8(),ResponseAvaliacaoFinanceira.class);
                } catch (JsonProcessingException e) {
                    logger.error("Erro ao tentar transformar corpo do retorno da API de avaliação financeira em um "
                    +ResponseAvaliacaoFinanceira.class.getSimpleName()+" "+e.getCause().toString());
                }
                meterRegistry.counter("propostas-criadas-nao-elegiveis").increment();
            }
        }
        return retorno;
    }
}
