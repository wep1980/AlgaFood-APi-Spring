package br.com.wepdev.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Entity
@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
public class FotoProduto {


	@Id // Nesse caso o Id nao e auto incremento
	@Column(name = "produto_id")
	@EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
	private Long id;

	//Uma foto produto tem um produto
	@OneToOne(fetch = FetchType.LAZY) // fetch = FetchType.LAZY -> Evita o carregamento desnecessario(selects) de produtos ao buscar apenas a foto do produto
	@MapsId // Produto e mapeado atraves da propriedade id da entidade FotoProduto(que é essa mesma entidade)
	private Produto produto; // Facilita na hora de pegar o produto pela foto

	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;

}
