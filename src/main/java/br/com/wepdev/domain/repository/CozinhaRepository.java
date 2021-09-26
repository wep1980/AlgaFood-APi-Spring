package br.com.wepdev.domain.repository;

import java.util.List;
import java.util.Optional;

import br.com.wepdev.infrastructure.repository.CustomJpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wepdev.domain.model.Cozinha;

/**
 * Inteface CozinhaRepository extende JpaRepository que ja possui varios metodos implementados, 
 * ele cria a implementacao em tempo de execucao
 * @author Waldir
 *
 */
@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long> {

	/*
	 * Referencia para a documentacao das keywords
	 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	 */
	
	//List<Cozinha> consultarPorNome(String nome); // Esse metodo nao funciona
	List<Cozinha> nome(String nome);//  // implementacao com Sprind Data do mesmo metodo acima
	List<Cozinha> findByNome(String nome); // implementacao com Sprind Data do mesmo metodo acima
	
	// implementacao com Spring-Data do mesmo metodo acima, pode ser adicionado qualquer nome entre o FIND e o BY, portanto que nao seja uma keyWords
	List<Cozinha> findQualquerNomeByNome(String nome);  
	
	Optional<Cozinha> findUnicaByNome(String nome);
	
	//---------------- Usando as keywords para definir critérios de query methods -------------------------------
	
	// Busca todas as cozinhas por nome com like utilizando os % % com a flag CONTAINING
	List<Cozinha> findTodasByNomeContaining(String nome);  
	
	
	/*
	 * Prefixos para iniciar o nome de um metodo. todos tem o mesmo funcionamento.
	 * findBy
	 * readBy
	 * getBy
	 * queryBy
	 * streamBy
	 */
	
	
	// Consulta a cozinha pelo nome exato, se existir returna true, se nao existir retorna false
	boolean existsByNome(String nome);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
