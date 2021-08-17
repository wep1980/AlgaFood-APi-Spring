package br.com.wepdev.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wepdev.domain.model.Cozinha;

/**
 * Inteface CozinhaRepository extende JpaRepository que ja possui varios metodos implementados, 
 * ele cria a implementacao em tempo de execucao
 * @author Waldir
 *
 */
@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long>{

	
	//List<Cozinha> consultarPorNome(String nome);
	
	
}
