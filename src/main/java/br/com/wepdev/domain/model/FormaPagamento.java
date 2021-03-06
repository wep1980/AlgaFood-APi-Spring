package br.com.wepdev.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
public class FormaPagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
	@EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
	private Long id;
	
	@Column(nullable = false)
	private String descricao;

	//Sempre que existir uma atualização desta entidade o jpa armazenara a data.
	@UpdateTimestamp // O hash(Etag) sera gerado atraves desse unico campo
	private OffsetDateTime dataAtualizacao;
	
	
}
