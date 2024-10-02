package com.eli.eli_food.api.controler;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eli.eli_food.core.validation.Groups;
import com.eli.eli_food.domain.exception.EstadoNaoEncontradaException;
import com.eli.eli_food.domain.exception.NegocioException;
import com.eli.eli_food.domain.model.Cidade;
import com.eli.eli_food.domain.repository.CidadeRepository;
import com.eli.eli_food.domain.service.CadastroCidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CadastroCidadeService cidadeService;
	
	@GetMapping
	public List<Cidade> listar(){
		return cidadeRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Cidade buscarCidade(@PathVariable Long id){

		return cidadeService.buscarOuFalhar(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cidade salvar(@RequestBody 
			@Validated(Groups.CadastroCidade.class)
			Cidade cidade){
		
//		try {
//			return cidadeService.salvar(cidade);
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cidadeNova);
//			
//		}catch (EntidadeNaoEncontradaException e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//		}
		
		try{
			return cidadeService.salvar(cidade);
		}catch(EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);//aqui ele vai retornar o codigo 400 pois o recurso existe , perem o parametro da requisição nao
		}
		
	}
	
	@PutMapping("/{id}")
	public Cidade atualizar(@PathVariable Long id, @RequestBody
			@Validated(Groups.CadastroCidade.class)
			Cidade cidade){
		
		
		
		try{
			Cidade cidadeAtual = cidadeService.buscarOuFalhar(id);
			BeanUtils.copyProperties(cidade, cidadeAtual, "id");
			
			return cidadeService.salvar(cidadeAtual);
		}catch(EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);//aqui ele vai retornar o codigo 400 pois o recurso existe , perem o parametro da requisição nao
		}
		
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id){
	//	try {
			cidadeService.deletar(id);
//			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		//} catch (EntidadeNaoEncontradaException e) {
		//	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		//}
	}
	
	
///////////////////////////////////////////////////////////////////////////////////////////
	
	/////////// Exception Handler a nivel de controlador. Só via funcionar aqui nesse controlador
//	@ExceptionHandler(EntidadeNaoEncontradaException.class)
//	public ResponseEntity<?> tratarEstadoNaoEncontradoException(EntidadeNaoEncontradaException e){
//		
//		Problema problema = Problema.builder()
//				.dataHora(LocalDateTime.now())
//				.mensagem(e.getMessage())
//				.build();
//		
//		return ResponseEntity.status(HttpStatus.NOT_FOUND)
//				.body(problema);
//		
//	}
//	
//	@ExceptionHandler(NegocioException.class)
//	public ResponseEntity<?> NegocioException(NegocioException e){
//		
//		Problema problema = Problema.builder()
//				.dataHora(LocalDateTime.now())
//				.mensagem(e.getMessage())
//				.build();
//		
//		return ResponseEntity.status(HttpStatus.NOT_FOUND)
//				.body(problema);
//		
//	}
	
}
