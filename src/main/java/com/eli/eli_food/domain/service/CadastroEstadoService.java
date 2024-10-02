package com.eli.eli_food.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eli.eli_food.domain.exception.EntidadeEmUsoException;
import com.eli.eli_food.domain.exception.EstadoNaoEncontradaException;
import com.eli.eli_food.domain.model.Estado;
import com.eli.eli_food.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}
	
	public void deletar(Long id) {
		try {
			buscarOuFalhar(id);
			estadoRepository.deleteById(id);
	
		} catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Estado de código %d não pode ser removida pois está em uso", id));
		}
	}
	
	public Estado buscarOuFalhar(Long id) {
		return  estadoRepository.findById(id)
				.orElseThrow(() ->  new EstadoNaoEncontradaException(id));
	}
	
	

}
