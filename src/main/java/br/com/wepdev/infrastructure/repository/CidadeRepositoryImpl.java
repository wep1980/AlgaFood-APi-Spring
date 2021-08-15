package br.com.wepdev.infrastructure.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.repository.CidadeRepository;

@Repository // TODO comentar sobre a anotacao
public class CidadeRepositoryImpl implements CidadeRepository{
	
	
	@PersistenceContext // Melhor pratica de injecao de dependencia do JPA, tem mais configuracoes
	private EntityManager entityManager;

	
	
	@Override
	public List<Cidade> listar(){
		
		// Consulta feita em JPQL, linguagem do JPA, faz consulta em objetos
		TypedQuery<Cidade> query = entityManager.createQuery("from Cidade", Cidade.class);
		
		return query.getResultList();
	}

	@Override
	public Cidade buscarPorId(Long id) {
		return entityManager.find(Cidade.class, id);
	}

	
	/**
	 * O marge se o objeto ja existir no banco de dados, ele atualiza o objeto
	 * e retorna o mesmo, senao ele salva um novo objeto
	 * @param cozinha
	 * @return
	 */
	@Override
	@Transactional
	public Cidade salvarOuAtualizar(Cidade cidade) {
		 return entityManager.merge(cidade);
	}

    @Transactional
    @Override
    public void remover(Long id) {
        Cidade cidade = buscarPorId(id);
        
        if (cidade == null) {
            throw new EmptyResultDataAccessException(1);
        }
        
        entityManager.remove(cidade);
    }
	

}
