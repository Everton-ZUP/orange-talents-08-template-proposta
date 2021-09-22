package br.com.zupacademy.propostas.exception;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ReturnError validacao (MethodArgumentNotValidException exception){

        List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        ReturnError erros = new ReturnError();

        globalErrors.forEach(erro -> erros.AddError(erro.getDefaultMessage()));
        fieldErrors.forEach(erro -> erros.addErrorField(erro.getField(),erro.getRejectedValue(),erro.getDefaultMessage()));

        return erros;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ErroRegraDeNegocio.class)
    public ReturnError validacaoRegraDeNegocio(ErroRegraDeNegocio erro){
        return erro.getErroDeRetorno();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotReadablePropertyException.class)
    public ReturnError validacao2(NotReadablePropertyException exception){
        ReturnError erros = new ReturnError();

        erros.AddError(exception.getLocalizedMessage());

        return erros;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ReturnError> validacaoExection(ResponseStatusException exception){
        ReturnError returnError = new ReturnError();
        returnError.AddError(exception.getReason());
        return new ResponseEntity(returnError,exception.getStatus());
    }

}
