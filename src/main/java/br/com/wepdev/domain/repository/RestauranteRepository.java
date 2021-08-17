package br.com.wepdev.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wepdev.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long>{

	/*
	 * Referencia para a documentacao das keywords
	 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	 */
	
    // Busca os restaurantes com taxa frete entre os valores inicial e final , pode ser adicionado qualquer nome entre o FIND e o BY, portanto que nao seja uma keyWords
	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);
	
	// Busca por qualquer parte do nome de um restaurante junto com ID da cozinha
	List<Restaurante> queryByNomeContainingAndCozinhaId(String nome, Long cozinhaId);
	
	/*
	 * Prefixos para iniciar o nome de um metodo. todos tem o mesmo funcionamento.
	 * findBy
	 * readBy
	 * getBy
	 * queryBy
	 * streamBy
	 */
	
	// First -> Retorna somente o primeiro restaurante. Poderia ser colocado qualquer descrição onde esta o QualquerCoisa
	Optional<Restaurante> findFirstQualquerCoisaByNomeContaining(String nome);
	
	// Top2 ->  Retorna somente os 2 primeiros restaurantes. Poderia ser colocado qualquer descrição onde esta o QualquerCoisa
	List<Restaurante> findTop2QualquerCoisaByNomeContaining(String nome);
	
	// Conta a quantidade de restaurantes pela cozinhaId selecionada
	int countByCozinhaId(Long cozinhaId);
	
}
