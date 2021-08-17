package br.com.wepdev.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wepdev.domain.model.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long>{

	
}
