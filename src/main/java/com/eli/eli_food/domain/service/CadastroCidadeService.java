package com.eli.eli_food.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.eli.eli_food.domain.exception.CidadeNaoEncontradaException;
import com.eli.eli_food.domain.exception.EntidadeEmUsoException;
import com.eli.eli_food.domain.model.Cidade;
import com.eli.eli_food.domain.model.Estado;
import com.eli.eli_food.domain.repository.CidadeRepository;
import com.eli.eli_food.domain.repository.EstadoRepository;

@Service
public class CadastroCidadeService {

	

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService estadoService;
	
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Estado estado = estadoService.buscarOuFalhar(estadoId);
		
		cidade.setEstado(estado);
		
		return cidadeRepository.save(cidade);
	}

	public void deletar(Long id) {
		try {
			buscarOuFalhar(id);
			cidadeRepository.deleteById(id);
		}catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException("Entidade não pode ser deletada pois está em uso.");
		}
		
		
	}
	
	public Cidade buscarOuFalhar(Long id) {
		return cidadeRepository.findById(id)
				.orElseThrow(() -> new CidadeNaoEncontradaException(id));
	}
	
	
	
}
