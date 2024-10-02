package com.eli.eli_food.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.NOT_FOUND) // aqui eu não precis usar esse response status pois o EntidadeNaoEncontradaException ja retorna ele
public  class EstadoNaoEncontradaException extends EntidadeNaoEncontradaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EstadoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	public EstadoNaoEncontradaException(Long id) {
		this(String.format("Não existe um cadastro de estado com o código %d.", id));
	}
	

	
}
