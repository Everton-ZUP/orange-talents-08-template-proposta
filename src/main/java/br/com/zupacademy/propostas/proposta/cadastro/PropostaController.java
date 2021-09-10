package br.com.zupacademy.propostas.proposta.cadastro;

import br.com.zupacademy.propostas.proposta.Proposta;
import br.com.zupacademy.propostas.proposta.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    private PropostaRepository propostaRepository;

    @PostMapping @ResponseStatus(HttpStatus.CREATED) @Transactional
    public ResponseEntity<?> cadastrarProposta(@RequestBody @Valid PropostaRequest formulario, UriComponentsBuilder uri){
        Proposta proposta = formulario.toModel();
        proposta = propostaRepository.save(proposta);

        URI location = uri.path("/propostas/{id}").build(proposta.getId());
        return ResponseEntity.created(location).build();
    }
}
