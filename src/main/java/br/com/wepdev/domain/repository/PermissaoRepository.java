package br.com.wepdev.domain.repository;

import br.com.wepdev.domain.model.Permissao;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoRepository extends CustomJpaRepository<Permissao, Long> {

	
}
