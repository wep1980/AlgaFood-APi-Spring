package br.com.wepdev.infrastructure.repository.spec;

import java.math.BigDecimal;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.wepdev.domain.model.Restaurante;

/**
 * Classe com estruta do DDD (Domain driven design) onde sao feitas implementacoes de repository para acesso ao Banco de dados.
 * Nesse momento nao e necessario colocar um @Component nessa classe, pois ele sera instanciado no controller
 * @author Waldir
 *
 */
public class RestauranteComFreteGratisSpec implements Specification<Restaurante>{
	private static final long serialVersionUID = 1L;

	
	
	@Override
	public Predicate toPredicate(Root<Restaurante> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		
		// Filtro onde taxaFrete é igual a 0
		return builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
	}

}
