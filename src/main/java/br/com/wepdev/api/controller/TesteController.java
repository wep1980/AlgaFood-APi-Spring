package br.com.wepdev.api.controller;

import static br.com.wepdev.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis; // Import static configurado em : window -> preferences -> favorites -> new type
import static br.com.wepdev.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.CozinhaRepository;
import br.com.wepdev.domain.repository.RestauranteRepository;
import br.com.wepdev.infrastructure.repository.spec.RestauranteSpecs;

@RestController
@RequestMapping("/teste")
public class TesteController {
	
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	
	/**
	 * O nome nao sera passado na Uri com @PathVariable e sim por @RequestParam (query Strings), exemplo abaixo.
	 * localhost:8080/teste/cozinhas/por-nome?nome=Brasileira 
	 * @param nome
	 * @return
	 */
	@GetMapping("/cozinha/por-nome") 
	public List<Cozinha> cozinhasPorNome(@RequestParam("nome") String nome){ // Fazendo binding do nome que vem da requisicao para a variavel nome
		return cozinhaRepository.findTodasByNomeContaining(nome);
	}
	
	/**
	 * Retorna um optional de cozinha, pois no repository ele tb retorna um optional de cozinha.
	 * caso nao seja passada nenhuma cozinha na requisição o Optional retorna um null
	 * @param nome
	 * @return
	 */
	@GetMapping("/cozinha/unico-por-nome") 
	public Optional<Cozinha> cozinhaPorNome(@RequestParam("nome") String nome){ // Fazendo binding do nome que vem da requisicao para a variavel nome
		return cozinhaRepository.findUnicaByNome(nome);
	}
	
	
	@GetMapping("/restaurantes/por-taxa-frete") // O @RequestParam ja vem embutido, nao necessario implementar explicitamente
	public List<Restaurante> restaurantesPorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal){ // Fazendo binding do nome que vem da requisicao para a variavel taxaInicial e taxaFinal
		return restauranteRepository.findByTaxaFreteBetween(taxaInicial , taxaFinal);
	}	
	
	
	@GetMapping("/restaurantes/por-nome-cozinhaid") // O @RequestParam ja vem embutido, nao necessario implementar explicitamente
	public List<Restaurante> restaurantesPorNomeCozinhaId(String nome, Long cozinhaId){ // Fazendo binding do nome que vem da requisicao para a variavel nome e cozinhaId
		//return restauranteRepository.queryByNomeContainingAndCozinhaId(nome , cozinhaId);
		return restauranteRepository.consultarPorNomeECozinhaId(nome , cozinhaId);
	}
	
	
	@GetMapping("/restaurantes/primeiro-restaurante") // O @RequestParam ja vem embutido, nao necessario implementar explicitamente
	public Optional<Restaurante> buscarPrimeiroRestaurante(String nome){ // Fazendo binding do nome que vem da requisicao para a variavel nome
		return restauranteRepository.findFirstQualquerCoisaByNomeContaining(nome);
	}	
	
	
	@GetMapping("/restaurantes/top2-restaurante") // O @RequestParam ja vem embutido, nao necessario implementar explicitamente
	public List<Restaurante> buscar2PrimeirosRestaurantes(String nome){ // Fazendo binding do nome que vem da requisicao para a variavel nome
		return restauranteRepository.findTop2QualquerCoisaByNomeContaining(nome);
	}
	
	@GetMapping("/cozinha/exists") 
	public boolean cozinhaExists(@RequestParam("nome") String nome){ // Fazendo binding do nome que vem da requisicao para a variavel nome
		return cozinhaRepository.existsByNome(nome);
	}
	
	@GetMapping("/restaurantes/count-por-cozinha") // O @RequestParam ja vem embutido, nao necessario implementar explicitamente
	public int restaurantesCountPorCozinhaId(Long cozinhaId){ // Fazendo binding do nome que vem da requisicao para a variavel nome
		return restauranteRepository.countByCozinhaId(cozinhaId);
	}
	
	
	@GetMapping("/restaurantes/nome-frete-jpql") // O @RequestParam ja vem embutido, nao necessario implementar explicitamente
	public List<Restaurante> restaurantesPorNomeETaxaFreteJpql(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){ // Fazendo binding do nome que vem da requisicao para a variavel taxaInicial e taxaFinal
		return restauranteRepository.find(nome, taxaFreteInicial , taxaFreteFinal);
	}	
	
	
	@GetMapping("/restaurantes/nome-frete-criteria") // O @RequestParam foi preciso para passar os valores na utilizacao do criteria
	public List<Restaurante> restaurantesPorNomeETaxaFreteCriteria(@RequestParam String nome, @RequestParam  BigDecimal taxaFreteInicial, @RequestParam  BigDecimal taxaFreteFinal){ // Fazendo binding do nome que vem da requisicao para a variavel taxaInicial e taxaFinal
		return restauranteRepository.find(nome, taxaFreteInicial , taxaFreteFinal);
	}
	
	
//	@GetMapping("/restaurantes/frete-gratis") // O @RequestParam foi preciso para passar os valores na utilizacao do criteria
//	public List<Restaurante> restaurantesPorFreteGratis(@RequestParam String nome){ // Fazendo binding do nome que vem da requisicao para a variavel taxaInicial e taxaFinal
//	    
//		/*
//		 * Padrao DDD(Domain driven design) Specification
//		 * Essa classe representa uma restricao, um filtro, que possui codigo Jpa
//		 */
//		var comFreteGratis = new RestauranteComFreteGratisSpec();
//		
//		/*
//		 * Padrao DDD(Domain driven design) Specification.
//		 * Essa classe representa uma restricao, um filtro, que possui codigo Jpa.
//		 */
//		var comNomeSemelhante = new RestauranteComNomeSemelhanteSpec(nome);  // Essa classe representa uma restricao, um filtro, que teria um codigo Jpa
//		
//		/*
//		 * O Repositorio precisa ser preprado para receber um specificartion.
//		 * Busca todos os restaurantes com frete gratis e com nome semelhante.
//		 */
//		return restauranteRepository.findAll(comFreteGratis.and(comNomeSemelhante));
//	}
	
	
	@GetMapping("/restaurantes/frete-gratis") // O @RequestParam foi preciso para passar os valores na utilizacao do criteria
	public List<Restaurante> restaurantesPorFreteGratis(@RequestParam String nome){ // Fazendo binding do nome que vem da requisicao para a variavel taxaInicial e taxaFinal
	
		/*
		 * RestauranteSpecs.comFreteGratis() -> Pelo fato de ser um restaurante com metodo estatico, o eclipse nao esta fazendo a importacao,
		 * foi feita a configuracao da classe RestauranteSpecs pegando o pacote br.com.wepdev.infrastructure.repository.spec e configurando em :
		 * window -> preferences -> favorites -> new type
		 */
		//return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
		
		return restauranteRepository.findComFreteGratis(nome);
	}
	
	
	@GetMapping("/restaurantes/primeiro")
	public Optional<Restaurante> restaurantePrimeiro(){
		return restauranteRepository.buscarPrimeiro();
	}
	
	
	@GetMapping("/cozinhas/primeiro")
	public Optional<Cozinha> cozinhaPrimeiro(){
		return cozinhaRepository.buscarPrimeiro();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
