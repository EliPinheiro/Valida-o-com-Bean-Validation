package com.eli.eli_food.api.controler;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eli.eli_food.domain.model.Estado;
import com.eli.eli_food.domain.repository.EstadoRepository;
import com.eli.eli_food.domain.service.CadastroEstadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/estados")
public class EstadoController {

	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService cadastroService;
	
	
	@GetMapping
	public List<Estado> listar(){
		return estadoRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Estado buscarPorId(@PathVariable Long id){
		return cadastroService.buscarOuFalhar(id);
	}
	
	@PostMapping
	public Estado adicionar(@RequestBody @Valid Estado estado) {
		return cadastroService.salvar(estado);
	}
	
	@PutMapping("/{id}")
	public Estado atualizar(@PathVariable Long id, @RequestBody @Valid Estado estado){
		Estado estadoAtual = cadastroService.buscarOuFalhar(id);
		BeanUtils.copyProperties(estado, estadoAtual, "id");
		return cadastroService.salvar(estadoAtual);
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id){
//		try {
			cadastroService.deletar(id);
			
//		}catch (EntidadeEmUsoException e) {
//			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
//	}
}
		}
