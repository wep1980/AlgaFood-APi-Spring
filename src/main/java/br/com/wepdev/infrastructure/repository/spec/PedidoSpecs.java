package br.com.wepdev.infrastructure.repository.spec;

import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.api.DTO.INPUT.PedidoFilterInputDTO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;


/**
 * Classe que recebe todas as specifications de pedido - Fabrica de specifications
 * @author Waldir
 *
 */
public class PedidoSpecs {


	/**
	 * Metodo que filtra pelas propriedades um pedido.
	 *
	 * Consulta implementada dentro de um bloco de codigo
	 * @param filtro
	 * @return
	 */
	public static Specification<Pedido> usandoFiltro(PedidoFilterInputDTO filtro) {

		return (root, query, builder) -> {
			root.fetch("restaurante").fetch("cozinha"); // Resolvendo problema do N+1 , muitos selects desnecessario
			root.fetch("cliente"); // Resolvendo problema do N+1 , muitos selects desnecessario

			var predicates = new ArrayList<Predicate>(); // Predicate e um filtro, funciona como se fosse um where

			//*** Adicionando predicates no arrayList de acordo com a regra de negocio ***

			if(filtro.getClienteId() != null){ //Se tiver um clineteId no filtro, adiciona ele no predicate
				// Adicionando um novo predicate, comparando a propriedade que vai ser consultada = "cliente", o valor que sera filtrado = getClienteId()
				predicates.add(builder.equal(root.get("cliente"), filtro.getClienteId()));
			}
			if(filtro.getRestauranteId() != null){//Se tiver um restauranteId no filtro, adiciona ele no predicate
				// Adicionando um novo predicate, comparando a propriedade que vai ser consultada = "restaurante", o valor que sera filtrado = getClienteId()
				predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
			}
			if(filtro.getDataCriacaoInicio() != null){ //Se tiver uma dataCriacaoInicio no filtro, adiciona ela no predicate
                // Adicionando um novo predicate, a dataCriacao do pedido tem que ser maior que a dataCriacaoInicio. greaterThanOrEqualTo -> maior ou igual a
				predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
			}
			if(filtro.getDataCriacaoFim() != null) {
				// Adicionando um novo predicate, a dataCriacao do pedido tem que ser menor ou igual dataCriacaoInicio. lessThanOrEqualTo -> menor que
				predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));
			}
			    // Transformando uma lista de Collection em um Array de 0 posições com os predicates preenchidos de acordo com a regra dos IFs
				return builder.and(predicates.toArray(new Predicate[0]));
		};
	}
}



