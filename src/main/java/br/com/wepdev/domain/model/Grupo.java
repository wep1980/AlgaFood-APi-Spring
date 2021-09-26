package br.com.wepdev.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Classe para criar grupos de acesso
 */
@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
@Entity
public class Grupo {
	
	@EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
	private Long id;
	
	
	@Column(nullable = false)
	private String nome;
	
	@ManyToMany // Muitas grupos tem muitas permissoes
	@JoinTable(name = "grupo_permissao", // Customozindo o nome da tabela criada em relacoes de muitos para muitos
			joinColumns = @JoinColumn(name ="grupo_id"), // Customozindo o nome da coluna que é a chave estrangeira que referencia a tabela restaurante
			inverseJoinColumns = @JoinColumn(name ="permissao_id"))  // Customozindo o nome da coluna que é a chave estrangeira que referencia a tabela Permissao
	private List<Permissao> permissoes = new ArrayList<>(); // Quando se cria uma instancia da lista, se evita o nullpointerexception ao instanciar uam cozinha

}
