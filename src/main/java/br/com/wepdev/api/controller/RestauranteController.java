package br.com.wepdev.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.wepdev.domain.exception.CozinhaNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.exception.RestauranteNaoEncontradoException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.RestauranteRepository;
import br.com.wepdev.domain.service.RestauranteService;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired 
	private RestauranteRepository restauranteRepository;

	
	
	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}
	
	
	@GetMapping("/{restauranteId}")
	public Restaurante buscar(@PathVariable Long restauranteId) {
		return restauranteService.buscarOuFalhar(restauranteId);

	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurante adicionar(@RequestBody Restaurante restaurante) {
		try {
			return restauranteService.salvar(restaurante);
		}catch (CozinhaNaoEncontradaException e) { // Exception caso a cozinha nao exista na hora de adicionar um restaurante
			throw new NegocioException(e.getMessage());
		}
	}


	@PutMapping("/{restauranteId}")
	public Restaurante atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
		try {
			//Busca o restaurante atual ou lança uma exception que esta com NOT.FOUND
			Restaurante restauranteAtual = restauranteService.buscarOuFalhar(restauranteId);
			// Copia a instancia de restaurante para restauranteAtual, exceto o id, formasPagamento, endereco, dataCadastro
			BeanUtils.copyProperties(restaurante, restauranteAtual, "id" , "formasPagamento", "endereco", "dataCadastro", "produtos");
			// Salva e retorna o corpo, e a resposta HTTP e enviada como 200 -> OK
			return restauranteService.salvar(restauranteAtual);
		} catch (CozinhaNaoEncontradaException e){ // Execessao lançada caso na hora de atualizar um restaurante a cozinha não exista
			throw new NegocioException(e.getMessage(), e); // e -> Mostra a causa da exception na representacao(POSTMAN)
		}

	}


	@PatchMapping("/{restauranteId}")
	public Restaurante atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos) {
		//Busca o restaurante atual ou lança uma exception que esta com NOT.FOUND
		Restaurante restauranteAtual = restauranteService.buscarOuFalhar(restauranteId);

		// Atribui os valores do campos para dentro do restauranteAtual
		merge(campos, restauranteAtual);
		
		return atualizar(restauranteId, restauranteAtual);
	}

	
	   /**
	     * Metodo que mescla os valores do postman com os valores armazendos no Banco de dados
		 * Expressao Lambda
		 * nomePropriedade -> String do Map
		 * valorPropriedade -> Object do Map
		 * REFLECTIONS -> INSPECIONA OBJETOS JAVA EM TEMPO DE EXECUÇÃO DE FORMA DINAMICA
		 */
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		// Serializa e converte objetos java em JSON e ao contrario tb
		ObjectMapper objectMapper = new ObjectMapper();
		// Criando um objeto Restaurante com os dados da origem(POSTMAN), JA CONVERTIDO E EVITANDO ERROS
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
		
		// Pegando as propriedades passadas pelo cliente
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			// field -> representa um atributo da classe Restaurante
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);  // Retorna a instancia de um campo
			field.setAccessible(true);// Acessa variavel private
			
			 // Buscando o valor da propriedade representada pelo field, dentro da instancia de restauranteOrigem
			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
			
//			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			
			// field -> atribuindo o valorPropriedade de instancia que é o nome, no nome da do restauranteDestino(Restaurante "nome") 
			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}

}
