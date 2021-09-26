package br.com.wepdev.domain.model;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
public class Usuario {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
	@EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String senha;
	
	
	/*
	 * LocalDateTime -> Representa uma data hora sem fuso horario(Sem TimeStamp)
	 * nullable = false ->  Propriedade obrigatoria
	 */
	@JsonIgnore// Na hora de serializar a propriedade cozinha sera ignorada
	@CreationTimestamp // No momento em que a entidade for criada pela primeira vez sera atribuida uma data e hora atual
	@Column(nullable = false, columnDefinition = "datetime") // Retira a precisao dos milisegundos
	private OffsetDateTime dataCadastro;
	
	
	
	@ManyToMany // Um usuario tem varios grupos
	@JoinTable(name = "usuario_grupo", // Customozindo o nome da tabela criada em relacoes de muitos para muitos
	           joinColumns = @JoinColumn(name = "usuario_id"), // Customozindo o nome da coluna que é a chave estrangeira que referencia a tabela usuario
	           inverseJoinColumns = @JoinColumn(name = "grupo_id"))  // Customozindo o nome da coluna que é a chave estrangeira que referencia a tabela Grupo
	private List<Grupo> grupos = new ArrayList<>();// Quando se cria uma instancia da lista, se evita o nullpointerexception ao instanciar uam cozinha



	public boolean senhaCoincideCom(String senha) {
		return getSenha().equals(senha);
	}

	public boolean senhaNaoCoincideCom(String senha) {
		return !senhaCoincideCom(senha);
	}
}
