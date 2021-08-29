package br.com.wepdev.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  * @JsonIgnore -> Ao utilizar o jsonIgnore a cozinha nao aparece na representacao(POSTMAN) mas os selects continuam sendo feitos
	 * A estrategia EagerLoad por padrão é utilizada quando as associacoes terminam com ToOne, exemplo manyToOne.
	 * EagerLoad -> carregamento ancioso, carregamento antecipado, toda vez que uma instancia de restaurante e carregada a partir do banco de dados ele carrega tb as 
	 * associacoes que contem EagerLoad.
	 * 
	 * 
 * Diferença entre inner join e left outer join -> 
	 * **** inner join : e feito quando se tem certeza que a tabela de associação não vai retornar valor null(nullable = false) ou seja no banco de dados esta notNull. No caso abaixo,
	 * sempre vai exister uma cozinha para restaurante. O nullable por padrão e true.
	 * **** left outer join : e feito quando a tabela de associacao recebe um nullable = true(Valor padrão) ou seja pode vim no resultado um valor null que mesmo assim o select sera
	 * realizado, exameplo: @ManyToOne
	                        @JoinColumn(name = "endereco_cidade_id") aqui ele recebe um nullable padrão, que é true. nullable = true
	                        private Cidade cidade;

	Nos bancos de dados relacioanais todos os relacionamentos que possuem muitos para muitos (*..*) precisam de uma tabela adicional.
	 * Deve ser sempre levado em conta o impacto dos relacionametos de muitos para muitos em uma REST APi
	 * Ao utilizar o jsonIgnore a formaPagamento nao aparece na representacao(POSTMAN) mas os selects continuam sendo feitos
	 *                         
 * @author Waldir
 *
 */
@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	

	@Column(name = "taxa_frete" , nullable = false) // Nao aceita valor nulo
	private BigDecimal taxaFrete;
	
	/**
	 * @JsonIgnore -> Ao utilizar o jsonIgnore a cozinha nao aparece na representacao(POSTMAN) mas os selects continuam sendo feitos
	 * A estrategia EagerLoad por padrão é utilizada quando as associacoes terminam com ToOne, exemplo manyToOne.
	 * EagerLoad -> carregamento ancioso, carregamento antecipado, toda vez que uma instancia de restaurante e carregada a partir do banco de dados ele carrega tb as 
	 * associacoes que contem EagerLoad.
	 * 
	 * Diferença entre inner join e left outer join -> 
	 * **** inner join : e feito quando se tem certeza que a tabela de associação não vai retornar valor null(nullable = false) ou seja no banco de dados esta notNull. No caso abaixo,
	 * sempre vai exister uma cozinha para restaurante. O nullable por padrão e true.
	 * **** left outer join : e feito quando a tabela de associacao recebe um nullable = true(Valor padrão) ou seja pode vim no resultado um valor null que mesmo assim o select sera
	 * realizado, exameplo: @ManyToOne
	                        @JoinColumn(name = "endereco_cidade_id") aqui ele recebe um nullable padrão, que é true. nullable = true
	                        private Cidade cidade;
	 */
	
	
	/*
	 * Por padrao todas as anotações terminadas com ToOne utilizam Eager, com Lazy as cozinhas so serao carregadas caso seja necessario.
	 * Como cozinha possui um @JsonIgnore e em nenhum lugar esta sendo feito um getCozinha().getQualquerMetodo(), nao vai mas ser feito o select cozinha
	 */
	//@JsonIgnore 
	@ManyToOne//(fetch = FetchType.LAZY) // Muitos - many(*) RESTAURANTES possuem uma - one(1) COZINHA.
	@JoinColumn(name = "cozinha_id", nullable = false) // A classe dona da associação e Restaurante, pois é onde fica a coluna cozinha_id
	private Cozinha cozinha; // Um restaurante possui 1 cozinha
	
	@Embedded // Esta classe esta sendo incorporada em Restaurante
	@JsonIgnore// Na hora de serializar a propriedade endereço sera ignorada
	private Endereco endereco;

	/*
	 * Nos bancos de dados relacioanais todos os relacionamentos que possuem muitos para muitos (*..*) precisam de uma tabela adicional.
	 * Deve ser sempre levado em conta o impacto dos relacionametos de muitos para muitos em uma REST APi
	 * Ao utilizar o jsonIgnore a formaPagamento nao aparece na representacao(POSTMAN) mas os selects continuam sendo feitos
	 * 
	 * Lazy Loading -> todos as tabelas criadas que possuem a propriedade toMany utilizam lazy Loading por padrao, 
	 * lazy e um carregamento por demanda, preguiçoso
	 * 
	 */
	@JsonIgnore // Na hora de serializar a propriedade cozinha sera ignorada
	@ManyToMany // Muitos restaurantes possuem muitas formas de pagamento, -- Dificilmente no ToMany e alterado o padrao que é Lazy para Eager
	@JoinTable(name = "restaurante_forma_pagamento",  // Customozindo o nome da tabela criada em relacoes de muitos para muitos
	           joinColumns = @JoinColumn(name ="restaurante_id"), // Customozindo o nome da coluna que é a chave estrangeira que referencia a tabela restaurante
	           inverseJoinColumns = @JoinColumn(name ="forma_pagamento_id"))  // Customozindo o nome da coluna que é a chave estrangeira que referencia a tabela formaPagamento
	private List<FormaPagamento> formasPagamento = new ArrayList<>(); // Quando se cria uma instancia da lista, se evita o nullpointerexception ao instanciar uam cozinha
	
	
	/*
	 * LocalDateTime -> Representa uma data hora sem fuso horario(Sem TimeStamp)
	 * nullable = false ->  Propriedade obrigatoria
	 * Ao utilizar o jsonIgnore a dataCadastro nao aparece na representacao(POSTMAN) mas os selects continuam sendo feitos
	 */
	@JsonIgnore// Na hora de serializar a propriedade cozinha sera ignorada
	@CreationTimestamp // No momento em que a entidade for criada pela primeira vez sera atribuida uma data e hora atual
	@Column(nullable = false, columnDefinition = "datetime") // Retira a precisao dos milisegundos
	private LocalDateTime dataCadastro;
	
	/*
	 * LocalDateTime -> Representa uma data hora sem fuso horario(Sem TimeStamp)
	 * nullable = false ->  Propriedade obrigatoria
	 * Ao utilizar o jsonIgnore a dataAtualizacao nao aparece na representacao(POSTMAN) mas os selects continuam sendo feitos
	 */
	@JsonIgnore// Na hora de serializar a propriedade cozinha sera ignorada
	@UpdateTimestamp // Atualiza a data Hora atual sempre que a entidade for atualizada
	@Column(nullable = false, columnDefinition = "datetime") // Retira a precisao dos milisegundos
	private LocalDateTime dataAtualizacao;
	
	
	@JsonIgnore// Na hora de serializar a propriedade cozinha sera ignorada
	@OneToMany(mappedBy = "restaurante") // Um restaurante possue muitos produtos
	private List<Produto> produtos = new ArrayList<>();
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
