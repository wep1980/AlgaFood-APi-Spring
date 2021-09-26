package br.com.wepdev.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


	//@NotBlank -> essa validação esta sendo feita no DTO de Input
	@Column(nullable = false)
	private String nome;


	//@PositiveOrZero -> essa validação esta sendo feita no DTO de Input
	//@NotNull -> essa validação esta sendo feita no DTO de Input
	@Column(name = "taxa_frete" , nullable = false) // Nao aceita valor nulo
	private BigDecimal taxaFrete;
	

	
	

	//@NotNull -> essa validação esta sendo feita no DTO de Input
	@ManyToOne//(fetch = FetchType.LAZY) // Muitos - many(*) RESTAURANTES possuem uma - one(1) COZINHA.
	@JoinColumn(name = "cozinha_id", nullable = false) // A classe dona da associação e Restaurante, pois é onde fica a coluna cozinha_id
	private Cozinha cozinha;//Um restaurante possui 1 cozinha

	
	@Embedded // Esta classe esta sendo incorporada em Restaurante
	private Endereco endereco;

	private Boolean ativo = Boolean.TRUE; // Sempre que um novo restaurante for instanciado por padrao ele seta ativo

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
	private Set<FormaPagamento> formasPagamento = new HashSet<>(); // Set -> É um conjunto que não aceita elementos duplicados, ou seja o mesmo id
	
	
	/*
	 * LocalDateTime -> Representa uma data hora sem fuso horario(Sem TimeStamp)
	 * nullable = false ->  Propriedade obrigatoria
	 * Ao utilizar o jsonIgnore a dataCadastro nao aparece na representacao(POSTMAN) mas os selects continuam sendo feitos
	 */
	@CreationTimestamp // No momento em que a entidade for criada pela primeira vez sera atribuida uma data e hora atual
	@Column(nullable = false, columnDefinition = "datetime") // Retira a precisao dos milisegundos
	private OffsetDateTime dataCadastro; // OffsetDateTime possui o offset em relação ao UTC
	
	/*
	 * LocalDateTime -> Representa uma data hora sem fuso horario(Sem TimeStamp)
	 * nullable = false ->  Propriedade obrigatoria
	 * Ao utilizar o jsonIgnore a dataAtualizacao nao aparece na representacao(POSTMAN) mas os selects continuam sendo feitos
	 */
	@UpdateTimestamp // Atualiza a data Hora atual sempre que a entidade for atualizada
	@Column(nullable = false, columnDefinition = "datetime") // Retira a precisao dos milisegundos
	private OffsetDateTime dataAtualizacao; // OffsetDateTime possui o offset em relação ao UTC
	
	

	@OneToMany(mappedBy = "restaurante") // Um restaurante possue muitos produtos
	private List<Produto> produtos = new ArrayList<>();



	public void ativar(){
		setAtivo(true);
	}

	public void inativar(){
		setAtivo(false);
	}


	public boolean removerFormaPagamento(FormaPagamento formaPagamento){
		return getFormasPagamento().remove(formaPagamento);
	}


	public boolean adicionarFormaPagamento(FormaPagamento formaPagamento){
		return getFormasPagamento().add(formaPagamento);
	}

}
