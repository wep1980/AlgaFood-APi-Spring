package br.com.wepdev.infrastructure.repository;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

/**
 * Implementação do repository generico
 * @author Waldir
 *
 */
public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID>{

	
	private EntityManager manager;
	


	/**
	 * Construtor que repassa o entityManager quando a classe e instanciada
	 * @param entityInformation
	 * @param entityManager
	 */
	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		
		this.manager = entityManager;
		
	}



	@Override
	public Optional<T> buscarPrimeiro() {
		  var jpql = "from " + getDomainClass().getName(); // Pegando o nome da classa na qual o repository vai estar usando no momento
		  
	        /*
	         *  getDomainClass() -> pegando a classe que esta utilizando o metodo
	         *  setMaxResults(1) -> Limita o resultado em apenas uma linha
	         *  getSingleResult() -> Retorna apenas 1 resultado
	         *   T entity -> O T pode ser qualquer entidade
	         */
	        T entity = manager.createQuery(jpql, getDomainClass()).setMaxResults(1).getSingleResult(); 
	        
			return Optional.ofNullable(entity);// Retorna um Optional com valor nulo ou com a entidade
	}


	/**
	 * Metodo que retira um objeto do gerenciamento do JPA, evita o sincronismo em um momento onde nao queremos, dessa forma evitamos erros
	 * @param entity
	 */
	@Override
	public void detach(T entity) {
       manager.detach(entity);
	}


}
