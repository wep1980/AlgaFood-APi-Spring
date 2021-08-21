package br.com.wepdev.infrastructure.repository.spec;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import br.com.wepdev.domain.model.Restaurante;

/**
 * Classe que recebe todas as specifications de restaurante - Fabrica de specifications
 * @author Waldir
 *
 */
public class RestauranteSpecs {
	
	
	/*
	 * Para nao precisar reornar uma instancia da classe RestauranteComFreteGratisSpec(), 
	 * pode ser criada uma classe anonima utilizando expressao lambda.
	 * 
	 */
	public static Specification<Restaurante> comFreteGratis(){
		//return new RestauranteComFreteGratisSpec();
		
		// Retornando a instancia de um Specification com lambda
		return (root, query, builder) -> builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
	}
	
	
	public static Specification<Restaurante> comNomeSemelhante(String nome){
		
		// Retornando a instancia de um Specification com lambda
		return (root, query, builder) -> builder.like(root.get("nome"), "%" + nome + "%");
	}

}
