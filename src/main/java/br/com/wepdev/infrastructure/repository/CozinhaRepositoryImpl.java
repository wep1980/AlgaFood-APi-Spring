package br.com.wepdev.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;

@Repository // TODO comentar sobre a anotacao
public class CozinhaRepositoryImpl implements CozinhaRepository{
	
	
	@PersistenceContext // Melhor pratica de injecao de dependencia do JPA, tem mais configuracoes
	private EntityManager entityManager;

	
	
	@Override
	public List<Cozinha> todas(){
		
		// Consulta feita em JPQL, linguagem do JPA, faz consulta em objetos
		TypedQuery<Cozinha> query = entityManager.createQuery("from Cozinha", Cozinha.class);
		
		return query.getResultList();
	}

	@Override
	public Cozinha porId(Long id) {
		return entityManager.find(Cozinha.class, id);
	}

	
	/**
	 * O marge se o objeto ja existir no banco de dados, ele atualiza o objeto
	 * e retorna o mesmo, senao ele salva um novo objeto
	 * @param cozinha
	 * @return
	 */
	@Override
	@Transactional
	public Cozinha adicionar(Cozinha cozinha) {
		 return entityManager.merge(cozinha);
	}

	@Override
	@Transactional
	public void remover(Cozinha cozinha) {
		cozinha = porId(cozinha.getId());
		entityManager.remove(cozinha);
	}
	

}
