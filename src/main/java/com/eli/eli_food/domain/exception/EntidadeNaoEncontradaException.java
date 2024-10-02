package com.eli.eli_food.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.NOT_FOUND) // se a exception não for tratada quando ela for lançada essa é a resposta
public abstract class EntidadeNaoEncontradaException extends NegocioException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	
}
