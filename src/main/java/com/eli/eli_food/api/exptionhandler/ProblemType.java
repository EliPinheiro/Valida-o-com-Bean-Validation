package com.eli.eli_food.api.exptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	
	DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos"),
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
	RECURSO_NAO_ENCONTRADO("/entidade-nao-encontrada", "Entidade nao encontrada"),
	ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
	PARAMETRO_INVALIDO("/parametro-invalido", "O parametro passado é inválido.");
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title){
		this.uri = "https://elifood.com.br" + path;
		this.title = title;
	}
}