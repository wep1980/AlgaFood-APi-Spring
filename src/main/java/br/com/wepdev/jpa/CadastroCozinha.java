package br.com.wepdev.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import br.com.wepdev.domain.model.Cozinha;

@Component
public class CadastroCozinha {

	@PersistenceContext // Melhor pratica de injecao de dependencia do JPA, tem mais configuracoes
	private EntityManager entityManager;
	
	
	public List<Cozinha> listar(){
		
		// Consulta feita em JPQL, linguagem do JPA, faz consulta em objetos
		TypedQuery<Cozinha> query = entityManager.createQuery("from Cozinha", Cozinha.class);
		
		return query.getResultList();
	}
}
