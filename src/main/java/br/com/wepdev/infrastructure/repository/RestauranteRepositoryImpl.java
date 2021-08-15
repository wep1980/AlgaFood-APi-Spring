package br.com.wepdev.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.RestauranteRepository;

@Repository // TODO comentar sobre a anotacao
public class RestauranteRepositoryImpl implements RestauranteRepository{
	
	
	@PersistenceContext // Melhor pratica de injecao de dependencia do JPA, tem mais configuracoes
	private EntityManager entityManager;

	
	
	@Override
	public List<Restaurante> listar(){
		
		// Consulta feita em JPQL, linguagem do JPA, faz consulta em objetos
		TypedQuery<Restaurante> query = entityManager.createQuery("from Restaurante", Restaurante.class);
		
		return query.getResultList();
	}

	@Override
	public Restaurante buscarPorId(Long id) {
		return entityManager.find(Restaurante.class, id);
	}

	
	/**
	 * O marge se o objeto ja existir no banco de dados, ele atualiza o objeto
	 * e retorna o mesmo, senao ele salva um novo objeto
	 * @param cozinha
	 * @return
	 */
	@Override
	@Transactional
	public Restaurante salvarOuAtualizar(Restaurante restaurante) {
		 return entityManager.merge(restaurante);
	}

	@Override
	@Transactional
	public void remover(Long id) {
		Restaurante restaurante = buscarPorId(id);
		entityManager.remove(restaurante);
	}
	

}
