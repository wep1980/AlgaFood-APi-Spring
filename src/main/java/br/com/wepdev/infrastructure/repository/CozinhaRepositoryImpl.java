package br.com.wepdev.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;

@Repository // alem de ser um component ela traz como beneficio o fato de ser uma tradutora de exception
public class CozinhaRepositoryImpl implements CozinhaRepository{
	
	
	@PersistenceContext // Melhor pratica de injecao de dependencia do JPA, tem mais configuracoes
	private EntityManager entityManager;

	
	
	@Override
	public List<Cozinha> listar(){
		
		// Consulta feita em JPQL, linguagem do JPA, faz consulta em objetos
		TypedQuery<Cozinha> query = entityManager.createQuery("from Cozinha", Cozinha.class);
		
		return query.getResultList();
	}

	@Override
	public Cozinha buscarPorId(Long id) {
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
	public Cozinha salvarOuAtualizar(Cozinha cozinha) {
		 return entityManager.merge(cozinha);
	}

	@Override
	@Transactional
	public void remover(Long id) {
		Cozinha cozinha = buscarPorId(id);
		if(cozinha == null) {
			throw new EmptyResultDataAccessException(1); // Excessao propria do spring , no parametro se coloca 1, pois se esperava no resultado uma cozinha
		}
		entityManager.remove(cozinha);
	}

	
	/**
	 * Metodo com consulta em JPQL, que e a linguagem de consulta do jpa
	 * like -> busca por uma parte do nome
	 * "%" + nome + "%" -> complementa o like buscando por qualquer parte do nome 
	 */
	@Override
	public List<Cozinha> consultarPorNome(String nome) {
		return entityManager.createQuery("from Cozinha where nome like :nome", Cozinha.class)
				.setParameter("nome", "%" + nome + "%")
				.getResultList();
	}
	

}
