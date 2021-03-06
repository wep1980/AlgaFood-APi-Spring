package br.com.wepdev.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.wepdev.domain.model.Restaurante;


/**
 * Ao herdar de RestauranteRepositoryQueries que é uma interface, automaticamente ele implementa os metodos sem a necessidade
 * da assinatura deles explicitamente.
 * 
 * RestauranteRepositoryQueries -> implementa essa interface que recebe a implementacao RestauranteRepositoryImpl onde ficam os metodos customizados.
 * JpaSpecificationExecutor<Restaurante> -> Implementa as classes de metodos customizados no padrao DDD(Domain driven design) Specifications
 * 
 * CustomJpaRepository<Restaurante, Long> -> Herda o repositorio generico
 * 
 * 
 * @author Waldir
 *
 */
@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries, JpaSpecificationExecutor<Restaurante>{

	/*
	 * Referencia para a documentacao das keywords
	 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	 */
	
	
	/*
	 * Assosiação de Restaurante com cozinha que é manyToOne é feito um FETCH automaticamente, 
	 * quando e feito um JOIN em uma relação manyToMany que é a de Restaurante com FormaPagamento o FETCH nao e feito automaticamente, 
	 * entao o correto é ser feito um LEFT JOIN FETCH, pois no caso de nao exister nenhuma formaPagamento para o restaurante associado, mesmo assim ele sera retornado,
	 * se fosse o JOIN FETCH nao retornaria nenhum restaurante
	 */
	@Query("from Restaurante r join fetch r.cozinha")
	List<Restaurante> findAll();
	
    // Busca os restaurantes com taxa frete entre os valores inicial e final , pode ser adicionado qualquer nome entre o FIND e o BY, portanto que nao seja uma keyWords
	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

	List<Restaurante> queryByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal); // Faz a mesma coisa do metodo acima, so muda o prefixo

	// Busca o primeiro restaurante de acordo com a letra escolhida(como se fosse um like)
	Optional<Restaurante> findFirstByNomeContaining(String nome);
	Optional<Restaurante> findFirstRestauranteByNomeContaining(String nome); // Faz a mesma coisa que o metodo acima, so que entre o First e o By tem o nome Restaurante, que poderia ser inlcusive qq nome.

	// Busca os 2 primeiros restaurantes de acordo com a letra escolhida(como se fosse um like)
	List<Restaurante> findTop2ByNomeContaining(String nome);

	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);
	
	// Busca por qualquer parte do nome de um restaurante junto com ID da cozinha
	//List<Restaurante> queryByNomeContainingAndCozinhaId(String nome, Long cozinhaId); // Metodo com nome muito grande ou ruim na legibilidade
	
	/*
	 * Foi criada uma pasta META-INF dentro de resources, la dentro tem um arquivo ORM.XML onde foi feita a configuracao da query abaixo,
	 * e para que o % % funcione ele foi concatenado dessa forma concat('%', :nome, '%') 
	 */
    //@Query("from Restaurante where nome like %:nome% and cozinha.id =:id") Faz o binding do id com @Param("id") de cozinha, se fosse cozinha.id =:cozinhaId nao precisaria de binding
	List<Restaurante> consultarPorNomeECozinhaId(String nome, @Param("id") Long cozinhaId); // Esse metodo faz o mesmo que o metodo acima que foi criado com query methods
	
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
	
	
	/**
	 * Assinatura do metodo implementado no RestauranteRepositoryImpl, 
	 * o problema e que dessa forma nao existe um vinculo entre a assinatura e o metodo.
	 * 
	 * @param nome
	 * @param taxaFreteInicial
	 * @param taxaFreteFinal
	 * @return
	 */
	//public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

	
}
