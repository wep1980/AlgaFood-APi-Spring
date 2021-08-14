package br.com.wepdev.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
public class Cidade {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
	@EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@JoinColumn(nullable = false)
	@ManyToOne // Muitas - many(*) CIDADES para um - one(1) ESTADO
	private Estado estado;

}
