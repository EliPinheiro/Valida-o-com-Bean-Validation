package com.eli.eli_food.api.exptionhandler;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter	
@Setter
@Builder
@JsonInclude(Include.NON_NULL) // So vai incluir na representação do erro os campos que não estiverem nulo
public class Problem {
	
	private Integer status;
	private String type;
	private String title;
	private String detail;
	
	private List<Object> objects; // Isso via retornar uma lista de campos invalidos quando formor fazer a validação com o bena validation
	
	
	private String userMessage;
	private LocalDateTime timastamp;
	
	@Getter
	@Builder
	public static class Field {
		private String name;
		private String userMessage;
	}


}


