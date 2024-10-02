package com.eli.eli_food.api.exptionhandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.flywaydb.core.internal.util.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.eli.eli_food.domain.exception.EntidadeEmUsoException;
import com.eli.eli_food.domain.exception.EntidadeNaoEncontradaException;
import com.eli.eli_food.domain.exception.NegocioException;
import com.eli.eli_food.domain.exception.ValidacaoException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import jakarta.validation.ConstraintViolationException;


@ControllerAdvice //Todas as excecoes serao tratadas aqui
public class ApiExceptionHandler extends ResponseEntityExceptionHandler { // essa classe  ResponseEntityExceptionHandler ja trata as exceções do spring MVC, mas podemos customizalas com sobre escrita
	
	@Autowired
	private MessageSource messageSource; //é geralmente usada em aplicações Spring para lidar com a internacionalização (i18n) de mensagens. O MessageSource permite que você acesse mensagens localizadas a partir de arquivos de propriedades, facilitando a adaptação do conteúdo para diferentes idiomas.
	
	private static final String MSG_ERRO_GENERICO_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. "
	            + "Tente novamente e se o problema persistir, entre em contato "
	            + "com o administrador do sistema.";

	
	//O erro handleMethodArgumentNotValid no Spring ocorre quando um argumento de método anotado com @Valid ou @Validated falha na validação. Isso geralmente acontece em controladores (controllers) ao tentar processar uma requisição com dados inválidos.
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		return handleValidationInternal(e,e.getBindingResult(), headers, status, request);
		}
	
	
	@ExceptionHandler( ValidacaoException.class )
	public ResponseEntity<Object> handleValidacaoException(ValidacaoException e, WebRequest request) {
		
	    return handleValidationInternal(e, e.getBindingResult(), new HttpHeaders(), 
	            HttpStatus.BAD_REQUEST, request);
	}
	


	private ResponseEntity<Object> handleValidationInternal(Exception e, BindingResult bindingResult,  HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		
		String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";

		 

	    
		List<Object> problemObjects = bindingResult.getFieldErrors().stream()
		    .map(fieldError -> {
		    	String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
		    	
		    	return Problem.Field.builder()
		        .name(fieldError.getField()) 
		        //.userMessage(fieldError.getDefaultMessage())// desse jeito aqui ele pega a mensagem padrao
		        .userMessage(message)
		        .build();})
		    .collect(Collectors.toList());
	    
	    
		Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail)
				.userMessage(detail)
				.objects(problemObjects)
				.build();
		
		return handleExceptionInternal(e, problem, headers, status, request);
	}

	
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<?> handleConstraintViolation(
	    ConstraintViolationException e, WebRequest request) {
		
		HttpStatusCode status = HttpStatus.BAD_REQUEST;
		
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		
		String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente";
		
		List<Object> problemObjects = e.getConstraintViolations().stream()
				.map(objectError -> {
					
					return Problem.Field.builder()
							.name(objectError.getRootBeanClass().getSimpleName())
							.userMessage(objectError.getMessage())
							.build();})
			    .collect(Collectors.toList());
					
					
		
		
//	    Map<String, String> errors = new HashMap<>();
//	    for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
//	        errors.put(violation.getPropertyPath().toString(), violation.getMessage());
//	    }


	    Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail)
				.userMessage(detail)
				.objects(problemObjects)
				.build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
		
	}
	
	
	@Override
		protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException e, HttpHeaders headers,
				HttpStatusCode status, WebRequest request) {
			
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		
		String valor = e.getResourcePath();
		String detail = String.format("O recurso '%s', que você tentou acessar é inexistente", valor );
		
		Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail)
				.userMessage(detail)
				.build();
		
		return handleExceptionInternal(e, problem, headers, status, request);
		
		
		}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException e, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		Object[] args = {e.getPropertyName(), e.getValue()};
		
		String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s' que é de um tipo inválido."
				+ " Corrija e informe um valor compatível com o tipo %s.",args[0], args[1], e.getRequiredType().getSimpleName());
		
		Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
				.build();
		
		
		return handleExceptionInternal(e, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		Throwable rootCause = ExceptionUtils.getRootCause(e); // Esse ExcetionUtil foi adicionada a partir de uma dependencia que adicionamos no pomm o commons lang 3. Isso via pegar na pilha de chamada a causo raiz do exception
	
		System.out.println(rootCause);
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidaFormatException((InvalidFormatException) rootCause, headers, (HttpStatus) status, request); // se for uma instacia de vai ter um tratamento especial 
		}else if(rootCause instanceof PropertyBindingException) {
			return handlePropertyBindingException((PropertyBindingException) rootCause, headers, (HttpStatus) status, request);
		}
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe";
		
		Problem problem = createProblemBuilder((HttpStatus) status, problemType , detail)
				.userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(e, problem, headers, status, request);
		
	}	
	
	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {

	   
	    String path = joinPath(ex.getPath());
	    
	    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
	    String detail = String.format("A propriedade '%s' não existe. "
	            + "Corrija ou remova essa propriedade e tente novamente.", path);

	    Problem problem = createProblemBuilder(status, problemType, detail)
	    		.userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
	    		.build();
	    
	    return handleExceptionInternal(ex, problem, headers, status, request);
	} 

	private ResponseEntity<Object> handleInvalidaFormatException(InvalidFormatException e, HttpHeaders headres, HttpStatus status, WebRequest request) {
	
		
		
//		e.getPath().forEach(ref -> System.out.println(ref.getFieldName()));
		
		String path = e.getPath().stream()
				.map(ref -> ref.getFieldName())
				.collect(Collectors.joining("."));
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, e.getValue(), e.getTargetType().getSimpleName());
		
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(e, problem, headres, status, request);
}

	// Método para tratar excecoes mais genericas que nao tem metodos especificos implementados aqui
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneralExceptions(Exception e, WebRequest request){
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		 String detail = MSG_ERRO_GENERICO_USUARIO_FINAL;
		Problem problem = createProblemBuilder(status, problemType, detail)
				.userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
				.build();
		
		return handleExceptionInternal(e, problem,  new HttpHeaders(), status, request);
	}

	
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handlerEstadoNaoEncontradoException(EntidadeNaoEncontradaException e, WebRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = e.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType , detail)
				.userMessage(detail)
				.build();
		return handleExceptionInternal(e, problem, new HttpHeaders(), status, request);
	
		
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handlerNegocioException(NegocioException e, WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = e.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType , detail)
				.userMessage(detail)
				.build();
		
		
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
		
	}
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handlerEntidadeEmUsoException(EntidadeEmUsoException e, WebRequest request){
		
		
		HttpStatus status = HttpStatus.CONFLICT;
		ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
		String detail = e.getMessage();
		
		Problem problem = createProblemBuilder(status, problemType , detail)
	    		.userMessage(detail)
	    		.build();
		
		return handleExceptionInternal(e, problem, new HttpHeaders(), HttpStatus.CONFLICT, request);
		
	}
	
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers,
		HttpStatusCode statusCode, WebRequest request) {
			
		if ( body == null) {
			body = Problem.builder()
					.timastamp(LocalDateTime.now())
					.title(statusCode.toString())
					.status(statusCode.value())
					.build();
		}else if ( body instanceof String) {
			body = Problem.builder()
					.timastamp(LocalDateTime.now())
					.title((String)body)
					.status(statusCode.value())
					.build();
		}
		
		
		
		
			return super.handleExceptionInternal(e, body, headers, statusCode, request);
		}
	
	
	private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail){
		
		return Problem.builder()
				.timastamp(LocalDateTime.now())
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(detail);
				
				
	}
	
	private String joinPath(List<Reference> references) {
	    return references.stream()
	        .map(ref -> ref.getFieldName())
	        .collect(Collectors.joining("."));
	} 
	
	
}
