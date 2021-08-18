package br.com.wepdev.infrastructure.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

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
		
		var jpql ="from Restaurante where nome like :nome and taxaFrete between :taxaInicial and :taxaFinal";
		
		return manager.createQuery(jpql, Restaurante.class)
				.setParameter("nome", "%" + nome + "%")
				.setParameter("taxaInicial", taxaFreteInicial)
				.setParameter("taxaFinal", taxaFreteFinal)
				.getResultList();
	}
	
	
	
	

}
