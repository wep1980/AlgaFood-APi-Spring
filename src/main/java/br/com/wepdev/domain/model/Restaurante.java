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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
	
	//@JoinColumn(name = "cozinha_id") // Forma de colocar nome em uma coluna que referencia uma chave estrangeira
	@JoinColumn(name = "cozinha_id", nullable = false) // A classe dona da associação e Restaurante, pois é onde fica a coluna cozinha_id
	@ManyToOne // Muitos - many(*) RESTAURANTES possuem uma - one(1) COZINHA
	private Cozinha cozinha; // Um restaurante possui uma cozinha
	
	@Embedded // Esta classe esta sendo incorporada em Restaurante
	@JsonIgnore// Na hora de serializar a propriedade cozinha sera ignorada
	private Endereco endereco;

	/*
	 * Nos bancos de dados relacioanais todos os relacionamentos que possuem muitos para muitos (*..*) precisam de uma tabela adicional.
	 * Deve ser sempre levado em conta o impacto dos relacionametos de muitos para muitos em uma REST APi
	 * 
	 */
	@JsonIgnore // Na hora de serializar a propriedade cozinha sera ignorada
	@ManyToMany // Muitos restaurantes possuem muitas formas de pagamento
	@JoinTable(name = "restaurante_forma_pagamento",  // Customozindo o nome da tabela criada em relacoes de muitos para muitos
	           joinColumns = @JoinColumn(name ="restaurante_id"), // Customozindo o nome da coluna que é a chave estrangeira que referencia a tabela restaurante
	           inverseJoinColumns = @JoinColumn(name ="forma_pagamento_id"))  // Customozindo o nome da coluna que é a chave estrangeira que referencia a tabela formaPagamento
	private List<FormaPagamento> formasPagamento = new ArrayList<>(); // Quando se cria uma instancia da lista, se evita o nullpointerexception ao instanciar uam cozinha
	
	
	/*
	 * LocalDateTime -> Representa uma data hora sem fuso horario(Sem TimeStamp)
	 * nullable = false ->  Propriedade obrigatoria
	 */
	@JsonIgnore// Na hora de serializar a propriedade cozinha sera ignorada
	@CreationTimestamp // No momento em que a entidade for criada pela primeira vez sera atribuida uma data e hora atual
	@Column(nullable = false, columnDefinition = "datetime") // Retira a precisao dos milisegundos
	private LocalDateTime dataCadastro;
	
	/*
	 * LocalDateTime -> Representa uma data hora sem fuso horario(Sem TimeStamp)
	 * nullable = false ->  Propriedade obrigatoria
	 */
	@JsonIgnore// Na hora de serializar a propriedade cozinha sera ignorada
	@UpdateTimestamp // Atualiza a data Hora atual sempre que a entidade for atualizada
	@Column(nullable = false, columnDefinition = "datetime") // Retira a precisao dos milisegundos
	private LocalDateTime dataAtualizacao;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
