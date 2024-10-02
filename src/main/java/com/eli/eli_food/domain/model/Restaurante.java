package com.eli.eli_food.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.eli.eli_food.core.validation.Groups;
import com.eli.eli_food.core.validation.Multiplo;
import com.eli.eli_food.core.validation.ValorZeroIncluiDescricao;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;

//CRIACAÇAO DE UM ANOTAÇÃO EM NÍVEL DE CLASSE
@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Restaurante {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@NotNull    // Isso defini que essa propriedade não pode ser null assim podemos usar o @Valid la no controlador, porem ele ainda aceita uma sstring vazia
//	@NotEmpty  // porem se estiver um monte de spaço ela ainda é aceita
	@NotBlank(groups = Groups.CadastroRestaurante.class)//, message = "É obrigatório um nome")
	@Column(nullable= false)
	private String nome;
	
	
//	@DecimalMin("-1")// so não funciona muito bem nesse caso pois ainda aceitaria valores negativos
    @NotNull
	@PositiveOrZero(groups = Groups.CadastroRestaurante.class) //O valor so pode ser positivo ou zero
	//@Multiplo(numero = 5, groups = Groups.CadastroRestaurante.class )
    @Column(name = "taxa_frete", nullable =false)
	private BigDecimal taxaFrete;

	
	@Valid //validação em cascata. Aqui será uma validação em cascate pois isso não ocorre por padrao. Se a classe cozinha tiver alguma validação e essa propriedade aqui não tiver essa anotação ent~]ao quanod formos adicionar um noco restaurante as anotações em cozinha nã0o funcionaram
//	@NotBlank(groups = Groups.CadastroRestaurante.class)
//	@JsonIgnore
//	@JsonIgnoreProperties("hibernateLazyInitializer")// como mudamos pra ser uma estrategia Lazy e a serializção não esta ignorada, vai lançar um exception, por isso temos que colocar isso pra ignorar essa propriedade que o proxy(casca) da propriedade cozinha tenta acessar.
	@JoinColumn(name = "cozinha_id", nullable= false) //Esse joinColumn é para nomear foreingKey de relacionamento entre entidades
	@ManyToOne//(fetch = FetchType.LAZY)// antes de olhar aqui retire a implentação com a query do interface do repositoio restaurante //se definirmos ela como lazy so vai carregar se precisar //Muito restaurante tem uma cozinha. O many é o resturante pois é a classe que estamos//(eagerLoad= carregameto ansioso) todas a terminações terminadas em toOne ela usa a strategia eager load por padrao ( ele ja vai fazer o select delas mesmo se tiver sendo ignorada
	private Cozinha cozinha;
	
	@JsonIgnore
	@Embedded //Esta falando que essa classe é uma parte da classe restaurante
	private Endereco endereco;

	@JsonIgnore
	@CreationTimestamp //Ele serve para atribuir a data quando a entidade for criada, não precisa passar na na geração do ddl
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataCadastro;
	
	@JsonIgnore
	@UpdateTimestamp //Sempre que a entidade for atualizada sepá alterada essa data
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime dataAtualizacao;

	
	@JsonIgnore
	@ManyToMany // So com essa anotação já é criada a tabela no banco de dados, porem vamos customizar com a anotação abaixo//Quanto termina com ToMany usa a estrategia lazy, so vai carregar se precisar ela é por demandaWQ@1
	@JoinTable(name = "restaurante_forma_pagamento",
			joinColumns = @JoinColumn(name = "restaurante_id"),
			inverseJoinColumns = @JoinColumn( name= "forma_pagamento_id"))
	private List<FormaPagamento> formasPagamento = new ArrayList<>();
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
	
	
	
}
