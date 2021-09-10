package br.com.wepdev.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.wepdev.Grupos;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
public class Estado {


	/**
	 * Em todas as anotações de validações de constraints existe uma propriedade chamada (groups = {}) que por padrão o grupo e default, @NotBlack(groups =)
	 * Um group recebe qualquer tipo de classe para indicar grupos para validação.
	 *
	 * como o id faz parte do Grupos.EstadoId.class ele faz parte desse grupo de validação, que no momento de inserir uma cidade torna obrigatorio,
	 *  passar o id de um estado, e tb nao deixa obrigatorio passar um id no cadastro de um estado, ja que o id e gerado automaticamente pelo banco de dados
	 */
	@NotNull(groups = Grupos.EstadoId.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
	@EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String nome;
	

}
