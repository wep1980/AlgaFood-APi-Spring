package br.com.wepdev.domain.repository;

import br.com.wepdev.domain.model.Grupo;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends CustomJpaRepository<Grupo, Long> {

	
}
