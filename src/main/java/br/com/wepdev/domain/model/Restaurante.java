package br.com.wepdev.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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


}
