package br.com.wepdev.domain.repository;

import java.util.List;
import java.util.Optional;

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

	
	//List<Cozinha> consultarPorNome(String nome); // Esse metodo nao funciona
	List<Cozinha> nome(String nome);//  // implementacao com Sprind Data do mesmo metodo acima
	List<Cozinha> findByNome(String nome); // implementacao com Sprind Data do mesmo metodo acima
	
	// implementacao com Sprind Data do mesmo metodo acima, pode ser adicionado qualquer nome entre o find e o By, portanto que nao seja uma keyWorld
	List<Cozinha> findQualquerNomeByNome(String nome);  
	
	Optional<Cozinha> findUnicaByNome(String nome);
	
}
