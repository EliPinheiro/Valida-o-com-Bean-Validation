package com.eli.eli_food.api.controler;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eli.eli_food.domain.model.Cozinha;
import com.eli.eli_food.domain.repository.CozinhaRepository;
import com.eli.eli_food.domain.service.CadastroCozinhaService;

import jakarta.validation.Valid;
// Produces ao nivel de classe, todos os metodos seguiram esse tipo
//@RequestMapping(value = "/cozinhas", produces =  MediaType.APPLICATION_JSON_VALUE)
@RequestMapping("/cozinhas")
@RestController // Junção das anotações controller e responseBody
public class CozinhaController {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cozinhaService;
	
	// Produces ele especifica qual o tipo de resposta esse metodo pode retornar . Não esquece que temos que adionar o dependencia no pom.xml **com.fasterxml.jackson.dataformat
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Cozinha> listar(){
		return cozinhaRepository.findAll();
	}
	

	
	
	
//	@ResponseStatus(HttpStatus.CONFLICT) // Com essa anotação a gente consegue mudar o status que será mostrado.
	@GetMapping("/{id}")
	public Cozinha buscar(@PathVariable("id") Long id) {  // o pathvcariable faz um bind(atribuição) com esse argumento 
//		Optional<Cozinha> cozinha =  cozinhaRepository.findById(id);
//
////		return ResponseEntity.ok(cozinha);		
////		HttpHeaders headers = new HttpHeaders();
////		headers.add(HttpHeaders.LOCATION, "http://api.elifood.com/novo_endereco");		
////		return ResponseEntity
////				.status(HttpStatus.FOUND)
////				.headers(headers)
////				.build();
//		if (cozinha.isEmpty()) {
//			return ResponseEntity.notFound().build();
//		}
//		return ResponseEntity.status(HttpStatus.OK).body(cozinha.get());
		
		return cozinhaService.buscarOuFalhar(id);
	}
	
	@PostMapping
	public ResponseEntity<Cozinha> adicionar(@RequestBody @Valid Cozinha cozinha){  // O requestBody via fazer o bind do corpo que passamos para a classe já definidas com o do corpo da requisição
		Cozinha cozinhaNova = cozinhaService.salvar(cozinha);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(cozinhaNova);
	}
	
	@PutMapping({"/{id}"})
	public Cozinha atualizar(@PathVariable Long id, 
			@RequestBody  @Valid Cozinha cozinha){
		
	
//		Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(id);
//		
//		if(cozinhaAtual.isPresent()) {
//			BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");
//		
//			Cozinha cozinhaSalva = cozinhaService.salvar(cozinhaAtual.get());
//			return ResponseEntity.ok(cozinhaSalva);	
//		}
//		return ResponseEntity.notFound().build();
		
		Cozinha cozinhaNova = cozinhaService.buscarOuFalhar(id);
		BeanUtils.copyProperties(cozinha, cozinhaNova, "id");
		return cozinhaService.salvar(cozinhaNova);
		
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id){
//		try {
			cozinhaService.excluir(id);
//		} catch (EntidadeNaoEncontradaException e) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//		}	catch (EntidadeEmUsoException e) {
//				throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage()); // O reponseStatusException é uma exceçao base, serve para não precisar ficar criando diferentes tipos de exceções
//		}
//			
//	}
	}

}
