package br.com.wepdev.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.wepdev.domain.model.Estado;
import br.com.wepdev.domain.repository.EstadoRepository;

@Repository // TODO comentar sobre a anotacao
public class EstadoRepositoryImpl implements EstadoRepository{
	
	
	@PersistenceContext // Melhor pratica de injecao de dependencia do JPA, tem mais configuracoes
	private EntityManager entityManager;

	
	
	@Override
	public List<Estado> listar(){
		
		// Consulta feita em JPQL, linguagem do JPA, faz consulta em objetos
		TypedQuery<Estado> query = entityManager.createQuery("from estado", Estado.class);
		
		return query.getResultList();
	}

	@Override
	public Estado buscarPorId(Long id) {
		return entityManager.find(Estado.class, id);
	}

	
	/**
	 * O marge se o objeto ja existir no banco de dados, ele atualiza o objeto
	 * e retorna o mesmo, senao ele salva um novo objeto
	 * @param estado
	 * @return
	 */
	@Override
	@Transactional
	public Estado salvarOuAtualizar(Estado estado) {
		 return entityManager.merge(estado);
	}

	@Override
	@Transactional
	public void remover(Estado estado) {
		estado = buscarPorId(estado.getId());
		entityManager.remove(estado);
	}
	

}
