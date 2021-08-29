package br.com.wepdev.domain.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	

	/*
	 * One -> uma cozinha, tem muitos (Many) restaurantes.
	 * Dentro de um relacionamento Bi-direcional cada cozinha sera serializada com uma lista de restaurantes, e dentro de cada restaurante e serializado uma lista de cozinhas,
	 * ou seja, um loop infinito sera criado
	 */
	//@JsonIgnore // Na hora de serializar a propriedade cozinha sera ignorada
	//@OneToMany(mappedBy = "cozinha") // mappedBy = "cozinha" -> Nome da propriedade onde foi feito o mapeamento em Restaurante para cozinha
	//private List<Restaurante> restaurantes = new ArrayList<>(); // Quando se cria uma instancia da lista, se evita o nullpointerexception ao instanciar uam cozinha
	

}
