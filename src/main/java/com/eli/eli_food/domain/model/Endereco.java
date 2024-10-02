package com.eli.eli_food.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Embeddable //Ela é uma classe icorporavel, ela é parte de outra classe e não uma classe em si
public class Endereco {
	
	@Column(name ="endereco_cep")
	private String cep;
	
	@Column(name ="endereco_logradouro")
	private String logradouro;
	
	@Column(name ="endereco_numero")
	private String numero;
	
	@Column(name ="endereco_complemento")
	private String complemento;
	
	@Column(name ="endereco_bairro")
	private String bairro;
	
	@JsonIgnoreProperties("hibernateLazyInitializer")// como mudamos pra ser uma estrategia Lazy e a serializção não esta ignorada, vai lançar um exception, por isso temos que colocar isso pra ignorar essa propriedade que o proxy(casca) da propriedade cozinha tenta acessar.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="endereco_cidade_id")
	private Cidade cidade;
	
	

}
