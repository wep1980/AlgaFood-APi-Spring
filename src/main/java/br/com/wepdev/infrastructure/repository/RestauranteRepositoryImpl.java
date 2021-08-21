package br.com.wepdev.infrastructure.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
	 * O metodo funciona independente dos parametros passados: nome, taxaFreteInicial ou taxaFreteFinal
	 * 
	 * @param nome
	 * @param taxaFreteInicial
	 * @param taxaFreteFinal
	 * @return
	 */
//	@Override
//	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
//		
//		var jpql = new StringBuilder(); // StringBuilder() -> Uma forma melhor de concatenar Strings
//		
//		var parametrosDaQuery = new HashMap< String , Object >();
//		
//		// Para resolver o problema se for passado so a taxaFrete inicial sem o nome e colocado where 0 = 0 que e sempre verdadeiro
//		jpql.append("from Restaurante where 0 = 0 "); // Se nao for passado nenhum restaurante o where 0 = 0 e verdadeiro e retorna todos os restaurantes
//		
//		if(StringUtils.hasLength(nome)) { // verifica se o nome esta vazio ou nulo
//			jpql.append("and nome like :nome ");
//			parametrosDaQuery.put("nome", "%" + nome + "%");
//		}
//		
//		if(taxaFreteInicial != null) {
//			jpql.append("and taxaFrete >= :taxaInicial ");
//			parametrosDaQuery.put("taxaInicial", taxaFreteInicial);
//		}
//		
//		if(taxaFreteFinal != null) {
//			jpql.append("and taxaFrete <= :taxaFinal ");
//			parametrosDaQuery.put("taxaFinal", taxaFreteFinal);
//		}
//		
//		// createQuery recebe jpql.toString() pq o jpql virou um StringBuilder()
//		TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);// TypedQuery() -> Instancia que recebe a consulta de createQuery
//		
//		// Para cada chave, valor : faça query.setParameter(chave, valor)
//		parametrosDaQuery.forEach((chave, valor) -> query.setParameter(chave, valor)); // forEach loop com expressao lambda
//		
//		return query.getResultList();
//			
//	}
	
	
	// ******* CRITERIA Api do JPA para criacao de querys de forma programatica, ele e burocratica, ideal para consultas complexas e dinamicas ****************************************
	
	
	/**
	 * O Metodo ficou dinamico, e obrigatorio passar todos os parametros
	 */
	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
		
		CriteriaBuilder builder = manager.getCriteriaBuilder(); // Criando uma instancia de CriteriaBuilder, é uma fabrica.
		
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class); // Monta uma query de Restaurante atraves do builder(Construtor de clausulas)
		Root<Restaurante> root = criteria.from(Restaurante.class);// Root representa a raiz do restaurante, as propreidades vao ser de restaurante
		
		var predicates = new ArrayList<Predicate>();
		
		if(StringUtils.hasText(nome)) { // Se tiver texto dentro da variavel nome
		// like(root.get("nome"), "%" + nome + "%") -> O primeiro parametro da propriedade e o nome, e a segunda propriedade e o valor
		predicates.add(builder.like(root.get("nome"), "%" + nome + "%")); // Predicate e um filtro, funciona como se fosse um where
	    }
		if(taxaFreteInicial != null) {
		// TaxaFrete tem q ser maior que ou igual a tacaFreteInicial
			predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
		}
		if(taxaFreteFinal != null) {
		// TaxaFrete tem q ser menor que ou igual a taxaFreteFinal
			predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
		}
		
		/*
		 * predicates.toArray(new Predicate[0]) -> Transformando a Lista em um array de 0 posições, o where recebe um array.
		 * Retorna a instancia de um array preenchido com os predicates que estao na lista
		 */
		criteria.where(predicates.toArray(new Predicate[0])); // recebe o filtro dos Predicates(Where)
		
		TypedQuery<Restaurante> query = manager.createQuery(criteria);
		
		return query.getResultList();
		
	}
	


	
	

}
