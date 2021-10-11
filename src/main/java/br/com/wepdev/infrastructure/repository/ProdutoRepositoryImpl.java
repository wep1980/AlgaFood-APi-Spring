package br.com.wepdev.infrastructure.repository;

import br.com.wepdev.domain.model.FotoProduto;
import br.com.wepdev.domain.repository.ProdutoRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {


    @PersistenceContext
    private EntityManager manager;



    @Transactional
    @Override
    public FotoProduto save(FotoProduto foto) {
        return manager.merge(foto);
    }
}
