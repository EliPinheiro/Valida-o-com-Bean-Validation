package com.eli.eli_food.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eli.eli_food.domain.exception.RestauranteNaoEncontradaException;
import com.eli.eli_food.domain.model.Cozinha;
import com.eli.eli_food.domain.model.Restaurante;
import com.eli.eli_food.domain.repository.CozinhaRepository;
import com.eli.eli_food.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {
	
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cozinhaService;
	
	public Restaurante salvar(Restaurante restaurante) {
		
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaService.buscarOuFalhar(cozinhaId);
		
		
		
		restaurante.setCozinha(cozinha);
		
		return restauranteRepository.save(restaurante);
	}
	
	public Restaurante buscarOuFalhar(Long id) {
		return restauranteRepository.findById(id)
				.orElseThrow(() -> new RestauranteNaoEncontradaException(id));
	}
	


}
