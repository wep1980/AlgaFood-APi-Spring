package br.com.wepdev.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.wepdev.domain.model.Cozinha;

@Component
public class CozinhaDAO {

	@PersistenceContext // Melhor pratica de injecao de dependencia do JPA, tem mais configuracoes
	private EntityManager entityManager;
	
	
	public List<Cozinha> listar(){
		
		// Consulta feita em JPQL, linguagem do JPA, faz consulta em objetos
		TypedQuery<Cozinha> query = entityManager.createQuery("from Cozinha", Cozinha.class);
		
		return query.getResultList();
	}
	
	/**
	 * O marge se o objeto ja existir no banco de dados, ele atualiza o objeto
	 * e retorna o mesmo, senao ele salva um novo objeto
	 * @param cozinha
	 * @return
	 */
	@Transactional
	public Cozinha adicionar(Cozinha cozinha) {
		 return entityManager.merge(cozinha);
	}
	
	
	public Cozinha buscarPorId(Long id) {
		return entityManager.find(Cozinha.class, id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
