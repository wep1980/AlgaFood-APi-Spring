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

	/**
	 * OBS : quando for necessario pegar uma fotoProduto atraves de um Produto ou seja , fazer um getProduto().getFotoProduto() que nao existira, o que pode ser feito é
	 * e buscar direto uma fotoProduto com uma consulta JPQL ou etc.. filtrando por produto, vai ser feito assim pq um relacionamento bi-direcional OneToOne e muito
	 * complexo e não vale a pena o esforço. segue o link para leitura a respeito : https://blog.algaworks.com/lazy-loading-com-mapeamento-onetoone/
	 */
	//Uma foto produto tem um produto
	@OneToOne(fetch = FetchType.LAZY) // fetch = FetchType.LAZY -> Evita o carregamento desnecessario(selects) de produtos ao buscar apenas a foto do produto
	@MapsId // Produto e mapeado atraves da propriedade id da entidade FotoProduto(que é essa mesma entidade)
	private Produto produto; // Facilita na hora de pegar o produto pela foto

	private String nomeArquivo;
	private String descricao;
	private String contentType;
	private Long tamanho;


	/**
	 * Metodo para retornar o id de um Restaurante, esse metodo foi criado para evitar o encadeamento abaixo no service(CatalogoFotoProdutoService)
	 * encadeamento -> Long restauranteId = foto.getProduto().getRestaurante().getId();
	 * @return
	 */
	public Long getRestauranteId(){
		if(getProduto() != null){
			return getProduto().getRestaurante().getId();
		}
		return null;
	}

}
