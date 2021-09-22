package br.com.zupacademy.propostas.cartao.viagem;

import br.com.zupacademy.propostas.cartao.ApiCartoes;
import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.cartao.CartaoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Map;

@RestController
public class AvisoViagemController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private AvisoViagemRepository avisoViagemRepository;

    @Autowired
    private ApiCartoes apiAvisos;

    @PostMapping("/cartoes/{id}/viagem")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public String adicionar(@PathVariable("id") String id, HttpServletRequest request,
                            @RequestBody @Valid AvisoViagemRequest body){
        Cartao cartao = cartaoRepository.findById(id).orElseThrow(()->{
            logger.error("cartão informado na url não encontrado"+id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND,"cartão não existe");
        });

        try{

            ResponseApiAviso respostaApi = apiAvisos.avisarSistemaLegado(
                    body.retornaCorpoRequisicaoApiExterna(),cartao.getNumeroCartao());
            System.out.println(respostaApi);

            if (respostaApi.sucesso()){
                AvisoViagem viagem = body.toModel(request,cartao);
                avisoViagemRepository.save(viagem);
                logger.info("aviso de viagem cadastrado com sucesso"+id+" "+viagem.getId());
                return "viagem cadastrada com sucesso!";
            }else{
                logger.error("Não foi possível notificar o sistema externo do aviso "+id);
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "não foi possível notificar o sistema externo");
            }
        }catch (FeignException | JsonProcessingException exception){
            logger.error("Não foi possível notificar o sistema externo do aviso "+id+" erro: "+ exception.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "não foi possível notificar o sistema externo");
        }
    }
}
