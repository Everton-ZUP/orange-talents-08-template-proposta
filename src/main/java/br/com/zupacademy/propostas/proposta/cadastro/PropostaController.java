package br.com.zupacademy.propostas.proposta.cadastro;

import br.com.zupacademy.propostas.proposta.EstadoProposta;
import br.com.zupacademy.propostas.proposta.Proposta;
import br.com.zupacademy.propostas.proposta.PropostaRepository;
import br.com.zupacademy.propostas.proposta.avaliacao.ApiAvaliacaoFinanceira;
import br.com.zupacademy.propostas.proposta.avaliacao.ResponseAvaliacaoFinanceira;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
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

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> cadastrarProposta(@RequestBody @Valid PropostaRequest formulario, UriComponentsBuilder uri) throws JsonProcessingException {
        Proposta proposta = formulario.toModel();

        proposta = propostaRepository.save(proposta);

        HashMap<String,String> formApi = new HashMap<>();
        formApi.put("documento",proposta.getDocumento());
        formApi.put("nome",proposta.getNome());
        formApi.put("idProposta",proposta.getId().toString());

        try {
            ResponseAvaliacaoFinanceira retorno = apiAvaliacaoFinanceira.fazerAvaliacaoFinaceira(formApi);
            proposta.setEstado(retorno.getStatusProposta());
        }catch (FeignException feignException){
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseAvaliacaoFinanceira retorno = objectMapper.readValue(feignException.contentUTF8(),ResponseAvaliacaoFinanceira.class);
            proposta.setEstado(retorno.getStatusProposta());
        }


        URI location = uri.path("/propostas/{id}").build(proposta.getId());
        return ResponseEntity.created(location).build();
    }
}
