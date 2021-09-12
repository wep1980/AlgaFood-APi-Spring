package br.com.wepdev.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import br.com.wepdev.core.validation.Grupos;
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

	@NotBlank
	@Column(nullable = false)
	private String nome;


	/**
	 * Converte do grupo default para o grupo EstadoId.class, na hora de validar estado, os propriedades no estado que tiverem o group EstadoId
	 * serao as unicas validadas por esse group. Resolve erros de validacão como por exemplo adicionar um novo estado sem a necessidade de passar um id, ja que o id
	 * e gerado automaticamente pelo banco
	 */
	@ConvertGroup(from = Default.class, to = Grupos.EstadoId.class)
	@ManyToOne // Muitas - many(*) CIDADES para um - one(1) ESTADO
	@JoinColumn(nullable = false)
	@NotNull
	@Valid // Valida as propriedades de estado, validação em cascata
	private Estado estado;

}
