package br.com.wepdev.domain.repository;

import org.springframework.stereotype.Repository;

import br.com.wepdev.domain.model.Cidade;

@Repository
public interface CidadeRepository extends CustomJpaRepository<Cidade, Long> {

	
}
