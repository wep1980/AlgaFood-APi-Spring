package br.com.wepdev.domain.model;


import br.com.wepdev.core.validation.Grupos;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

//@JsonRootName("gastronomia") // altera o nome que fica entre <> na representação(POSTMAN)
@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
@Entity
public class Cozinha {

	/**
	 * Em todas as anotações de validações de constraints existe uma propriedade chamada (groups = {}) que por padrão o grupo e default, @NotBlack(groups =)
	 * Um group recebe qualquer tipo de classe para indicar grupos para validação.
	 *
	 * Como o id faz parte do Grupos.CozinhaId.class ele faz parte desse grupo de validação, que no momento de inserir um Restaurante torna obrigatorio,
	 * passar o id de uma cozinha, e tb nao deixa obrigatorio passar um id no cadastro de uma cozinha ja que o id e gerado automaticamente pelo banco de dados
	 */
	@NotNull(groups = Grupos.CozinhaId.class)// como o id faz parte do Grupos.CadastroRestaurante.class ele passa por esse grupo de validação
	@EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
	private Long id;
	
	//@JsonIgnore // Igona esse campo na hora de gerar a representacao(POSTMAN)
	//@JsonProperty(value = "nomeJsonProperty") // Nome que aparecera na representação(POSTMAN)
	@Column(nullable = false)
	@NotBlank// Nao aceita nulo, nem vazio, nem espaços
	private String nome;

	/*
	 * One -> uma cozinha, para varios (Many) restaurantes.
	 * Dentro de um relacionamento Bi-direcional cada cozinha sera serializada com uma lista de restaurantes, e dentro de cada restaurante e serializado uma lista de cozinhas,
	 * ou seja, um loop infinito sera criado
	 */
	//@JsonIgnore // Na hora de serializar a propriedade cozinha sera ignorada
	//@OneToMany(mappedBy = "cozinha") // mappedBy = "cozinha" -> Nome da propriedade onde foi feito o mapeamento em Restaurante para cozinha
	//private List<Restaurante> restaurantes = new ArrayList<>(); // Quando se cria uma instancia da lista, se evita o nullpointerexception ao instanciar uam cozinha
	@OneToMany(mappedBy = "cozinha") // Uma cozinha para varios restaurantes
	private List<Restaurante> restaurantes = new ArrayList<>();

}
