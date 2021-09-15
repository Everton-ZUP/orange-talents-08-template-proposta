package br.com.zupacademy.propostas.proposta;

import br.com.zupacademy.propostas.proposta.avaliacao.ApiAvaliacaoFinanceira;
import br.com.zupacademy.propostas.proposta.avaliacao.ResponseAvaliacaoFinanceira;
import br.com.zupacademy.propostas.proposta.consulta.PropostaConsultaResponse;
import br.com.zupacademy.propostas.proposta.cadastro.PropostaRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private ApiAvaliacaoFinanceira apiAvaliacaoFinanceira;

    @GetMapping("/{id}") @ResponseStatus(HttpStatus.OK)
    public PropostaConsultaResponse buscarProposta(@PathVariable("id") Long idProposta){
        Proposta proposta = propostaRepository.findById(idProposta).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"Proposta não encontrada"));
        return new PropostaConsultaResponse(proposta);
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarProposta(@RequestBody @Valid PropostaRequest formulario, UriComponentsBuilder uri) throws JsonProcessingException {
        Proposta proposta = formulario.toModel();

        proposta = propostaRepository.save(proposta);

        HashMap<String,String> formApi = new HashMap<>();
        formApi.put("documento",proposta.getDocumento());
        formApi.put("nome",proposta.getNome());
        formApi.put("idProposta",proposta.getId().toString());

        ResponseAvaliacaoFinanceira retorno;
        try {
            retorno = apiAvaliacaoFinanceira.fazerAvaliacaoFinaceira(formApi);
        }catch (FeignException feignException){
            if (feignException.contentUTF8().isEmpty()){
                throw feignException;
            }else{
                retorno = new ObjectMapper().readValue(feignException.contentUTF8(),ResponseAvaliacaoFinanceira.class);
            }
        }

        proposta.setEstado(retorno.getStatusProposta());
        proposta = propostaRepository.save(proposta);

        URI location = uri.path("/propostas/{id}").build(proposta.getId());
        return ResponseEntity.created(location).build();
    }
}
