package br.com.wepdev.domain.repository;

import br.com.wepdev.infrastructure.repository.CustomJpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wepdev.domain.model.Estado;

@Repository
public interface EstadoRepository extends CustomJpaRepository<Estado, Long> {

	
}
