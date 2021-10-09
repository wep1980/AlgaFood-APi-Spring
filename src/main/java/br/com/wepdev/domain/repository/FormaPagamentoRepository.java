package br.com.wepdev.domain.repository;

import br.com.wepdev.domain.model.FormaPagamento;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaPagamentoRepository extends CustomJpaRepository<FormaPagamento, Long> {

	
}
