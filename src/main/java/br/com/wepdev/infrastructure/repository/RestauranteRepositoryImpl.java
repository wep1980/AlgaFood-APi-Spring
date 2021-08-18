package br.com.wepdev.infrastructure.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.RestauranteRepositoryQueries;

/**
 * Classe de implementacao de um repositorio customizado, aqui pode ser incluindo logica nos metodos,
 * deixar os metodos mais dinamicos e mais complexos.
 * 
 * E Obrigatorio que a classe tenha o sulfixo Impl para que a classe possa ser utilizada na Interface do RestauranteRepository.
 * 
 * Essa classe é um Bean Spring
 * 
 * @author Waldir
 *
 */
@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {
	
	
	@PersistenceContext
	private EntityManager manager;
	
	
	/**
	 * É necessario colocar a assinatura do metodo na Interface RestauranteRepository
	 * 
	 * @param nome
	 * @param taxaFreteInicial
	 * @param taxaFreteFinal
	 * @return
	 */
	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
		
		var jpql = new StringBuilder(); // StringBuilder() -> Uma forma melhor de concatenar Strings
		
		var parametrosDaQuery = new HashMap< String , Object >();
		
		// Para resolver o problema se for passado so a taxaFrete inicial sem o nome e colocado where 0 = 0 que e sempre verdadeiro
		jpql.append("from Restaurante where 0 = 0 "); // Se nao for passado nenhum restaurante o where 0 = 0 e verdadeiro e retorna todos os restaurantes
		
		if(StringUtils.hasLength(nome)) { // verifica se o nome esta vazio ou nulo
			jpql.append("and nome like :nome ");
			parametrosDaQuery.put("nome", "%" + nome + "%");
		}
		
		if(taxaFreteInicial != null) {
			jpql.append("and taxaFrete >= :taxaInicial ");
			parametrosDaQuery.put("taxaInicial", taxaFreteInicial);
		}
		
		if(taxaFreteFinal != null) {
			jpql.append("and taxaFrete <= :taxaFinal ");
			parametrosDaQuery.put("taxaFinal", taxaFreteFinal);
		}
		
		// createQuery recebe jpql.toString() pq o jpql virou um StringBuilder()
		TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);// TypedQuery() -> Instancia que recebe a consulta de createQuery
		
		// Para cada chave, valor : faça query.setParameter(chave, valor)
		parametrosDaQuery.forEach((chave, valor) -> query.setParameter(chave, valor)); // forEach loop com expressao lambda
		
		return query.getResultList();
			
	}
	
	
	
	

}
