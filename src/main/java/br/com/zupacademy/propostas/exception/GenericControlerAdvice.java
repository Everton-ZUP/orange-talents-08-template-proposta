package br.com.zupacademy.propostas.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class GenericControlerAdvice {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ReturnError validacao (MethodArgumentNotValidException exception){

        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        ReturnError erros = new ReturnError();

        globalErrors.forEach(erro -> erros.AddError(erro.getDefaultMessage()));
        fieldErrors.forEach(erro -> {
                     erros.addErrorField(erro.getField(),erro.getRejectedValue(),erro.getDefaultMessage()
                            ,"",HttpStatus.BAD_REQUEST.toString());
                    logger.error("Erro ao Validar campo de requisição "+erro.getField()+" "+erro.getDefaultMessage());
                });
        return erros;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ErroRegraDeNegocio.class)
    public ReturnError validacaoRegraDeNegocio(ErroRegraDeNegocio erro){
        logger.error("Erro em regra de negócio "+erro.getErroDeRetorno().toString());
        return erro.getErroDeRetorno();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotReadablePropertyException.class)
    public ReturnError validacao2(NotReadablePropertyException exception){
        ReturnError erros = new ReturnError();

        erros.AddError(exception.getLocalizedMessage());
        logger.error("Erro ao transformar Objeto em JSON "+exception.getLocalizedMessage());
        return erros;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ReturnError validaConstrucaoJson(HttpMessageNotReadableException exception){
        ReturnError returnError = new ReturnError();
        returnError.addErrorField("","", exception.getLocalizedMessage(),"",HttpStatus.BAD_REQUEST.toString());
        return returnError;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ReturnError> validacaoExection(ResponseStatusException exception){
        ReturnError returnError = new ReturnError();
        returnError.addErrorField("","",exception.getReason(),"",exception.getStatus().toString());
        logger.error("Erro "+exception.getReason()+" "+exception.getStatus());
        return new ResponseEntity(returnError, exception.getStatus());
    }

}
