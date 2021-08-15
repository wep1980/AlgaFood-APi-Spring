package br.com.wepdev.domain.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@JsonRootName("gastronomia") // altera o nome que fica entre <> na representação(POSTMAN)
@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
@Entity
public class Cozinha {
	
	@EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
	private Long id;
	
	//@JsonIgnore // Igona esse campo na hora de gerar a representacao(POSTMAN)
	//@JsonProperty(value = "nomeJsonProperty") // Nome que aparecera na representação(POSTMAN)
	@Column(nullable = false)
	private String nome;
	

}
