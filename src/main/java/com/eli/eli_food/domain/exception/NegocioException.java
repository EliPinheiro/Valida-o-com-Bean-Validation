package com.eli.eli_food.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.BAD_REQUEST) // se a exception não for tratada quando ela for lançada essa é a resposta
public class NegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NegocioException(String mensagem) {
        super(mensagem);
    }
    
    public NegocioException(String mensagem, Throwable causa) {// aqui ele vai lançar a causa tambem.
        super(mensagem, causa);
    }   
}