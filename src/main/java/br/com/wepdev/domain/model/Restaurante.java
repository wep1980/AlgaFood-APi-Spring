package br.com.wepdev.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import br.com.wepdev.core.validation.Grupos;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
 *
 * @ValorZeroIncluiDescricao -> Anotação de classe customizada, onde 2 ou mais propriedades precisa ser validadas,
 * Verifica Se taxa frete for igual a 0, se for, verifica se nome possui a descrição -> Frete Grátis
 *
 * RestauranteMixin -> classe de configuracao das propriedades aqui de restaurante que possum anotações do Jackson
 */

//@ValorZeroIncluiDescricao(valorField = "taxaFrete" , descricaoField = "nome" , descricaoObrigatoria = "Frete Grátis")
@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
	private Long id;

	//@NotNull // O bean validation e executado antes de fazer as validações no banco de dados, aceita o valor vazio
	//@NotEmpty // Nao aceita nulu nem vazio, mas aceita com espaços
	//@NotBlank -> Nao aceita nulo, nem vazio, nem espaços.
	@NotBlank // como o nome faz parte do Grupos.CadastroRestaurante.class ele passa por essa validação
	@Column(nullable = false)
	private String nome;

	//@Multiplo(numero = 5) // Anotacao customizada com codigo java(Regras)
	//@TaxaFrete
    //@DecimalMin("0") // Valor minimo da taxa frete e 0 zero
	// @PositiveOrZero -> O valor tem ser positivo ou zero 0
	@PositiveOrZero// como o taxaFrete faz parte do Grupos.CadastroRestaurante.class ele passa por esse validação
	@Column(name = "taxa_frete" , nullable = false) // Nao aceita valor nulo
	@NotNull
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
	//(groups=Grupos.CadastroRestaurante.class) -> Cozinha tb faz parte do Grupos.CadastroRestaurante.class as propriedades dela com (grupo = ), tb passam pela validação
	@NotNull// @NotNull nesse caso nao poder ser nula pq tem que existir uma instancia de cozinha.
	@Valid // Valida as propriedades de cozinha, validação em cascata
	/**
	 * Converte do grupo default para o grupo CozinhaId.class, na hora de validar cozinha, os propriedades na cozinha que tiverem o group CadastroRestaurante
	 * serao as unicas validadas por esse group. Resolve erros de validacão como por exemplo adicionar uma nova cozinha sem a necessidade de passar um id, ja que o id
	 * e gerado automaticamente pelo banco
	 */
	@ConvertGroup(from = Default.class, to = Grupos.CozinhaId.class)
	private Cozinha cozinha;//Um restaurante possui 1 cozinha

	
	@Embedded // Esta classe esta sendo incorporada em Restaurante
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
	@CreationTimestamp // No momento em que a entidade for criada pela primeira vez sera atribuida uma data e hora atual
	@Column(nullable = false, columnDefinition = "datetime") // Retira a precisao dos milisegundos
	private LocalDateTime dataCadastro;
	
	/*
	 * LocalDateTime -> Representa uma data hora sem fuso horario(Sem TimeStamp)
	 * nullable = false ->  Propriedade obrigatoria
	 * Ao utilizar o jsonIgnore a dataAtualizacao nao aparece na representacao(POSTMAN) mas os selects continuam sendo feitos
	 */
	@UpdateTimestamp // Atualiza a data Hora atual sempre que a entidade for atualizada
	@Column(nullable = false, columnDefinition = "datetime") // Retira a precisao dos milisegundos
	private LocalDateTime dataAtualizacao;
	
	

	@OneToMany(mappedBy = "restaurante") // Um restaurante possue muitos produtos
	private List<Produto> produtos = new ArrayList<>();


}
