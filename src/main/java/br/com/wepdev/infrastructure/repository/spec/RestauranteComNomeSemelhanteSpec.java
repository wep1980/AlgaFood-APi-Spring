package br.com.wepdev.infrastructure.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.wepdev.domain.model.Restaurante;
import lombok.AllArgsConstructor;

/**
 * Classe com estruta do DDD (Domain driven design) onde sao feitas implementacoes de repository para acesso ao Banco de dados.
 * Nesse momento nao e necessario colocar um @Component nessa classe, pois ele sera instanciado no controller
 * @author Waldir
 *
 */
@AllArgsConstructor // Lombok -> Criando um contrutor com todas as variaveis da classe.
public class RestauranteComNomeSemelhanteSpec implements Specification<Restaurante>{
	private static final long serialVersionUID = 1L;

	private String nome;
	
	@Override
	public Predicate toPredicate(Root<Restaurante> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		
		// Filtro onde taxaFrete é igual a 0
		return builder.like(root.get("nome"), "%" + nome + "%");
	}

}
