package br.com.wepdev.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
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

	
	
	    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Produz Json - Anotacao colocada no escopo da classe
		public List<Restaurante> listar() {
			return restauranteRepository.listar();
		}
	    
	    
		@GetMapping("/{restauranteId}")
		public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long restauranteId) {
			Restaurante restaurante = restauranteRepository.buscarPorId(restauranteId);
			
			if(restaurante != null) {
				return ResponseEntity.ok(restaurante);
			}
			//return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Se nao existir o Id retorna um NOT FOUND e sem corpo.
			return ResponseEntity.notFound().build(); // Atalho para a linha acima
		}
		
		
		/**
		 * ResponseEntity<?> salvarOuAtualizar -> <?> Foi colocado um coringa para que a resposta no corpo fosse de qualquer tipo, 
		 * nao so de restaurante, ja que a resposta deve ser uma string pois uma message de erro sera enviada atraves do getMessage() caso aconteça
		 * @param restaurante
		 * @return
		 */
		@PostMapping
		public ResponseEntity<?> salvar(@RequestBody Restaurante restaurante){
			try {
				restaurante = restauranteService.salvar(restaurante);
				return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
			} catch (EntidadeNaoEncontradaException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		
		
		/**
		 * ResponseEntity<?> salvarOuAtualizar -> <?> Foi colocado um coringa para que a resposta no corpo fosse de qualquer tipo, 
		 * nao so de restaurante, ja que a resposta deve ser uma string pois uma message de erro sera enviada atraves do getMessage() caso aconteça
		 * @param restauranteId
		 * @param restaurante
		 * @return
		 */
	    @PutMapping("/{restauranteId}")
	    public ResponseEntity<?> atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
	        try {
				Restaurante restauranteAtual = restauranteRepository.buscarPorId(restauranteId);
				
				if (restauranteAtual != null) {
					BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
					
					restauranteAtual = restauranteService.salvar(restauranteAtual);
					return ResponseEntity.ok(restauranteAtual);
				}
				return ResponseEntity.notFound().build();
			
			} catch (EntidadeNaoEncontradaException e) {
				return ResponseEntity.badRequest()
						.body(e.getMessage());
			}
	    }
	    
	    
		@DeleteMapping("/{restauranteId}")
		public ResponseEntity<Restaurante> remover(@PathVariable Long restauranteId){
			try {
				 restauranteService.remover(restauranteId);
				 return ResponseEntity.noContent().build(); // Como o recurso ja foi removido na ha necessidade de retornar um corpo
				 
			} catch (EntidadeNaoEncontradaException e) { // Excessao de negocio customizada
				//AO TENTAR REMOVER UMA COZINHA VINCULADA AO RESTAURANTE OCORRE UM ERRO DE VIOLAÇÃO NO BD, RESPOSTA HTTP SERA ESSA.
				return ResponseEntity.noContent().build(); // Como o recurso ja foi removido na ha necessidade de retornar um corpo 
				
			} catch (EntidadeEmUsoException e) { // Excessao de negocio customizada
				//AO TENTAR REMOVER UMA COZINHA VINCULADA AO RESTAURANTE OCORRE UM ERRO DE VIOLAÇÃO NO BD, RESPOSTA HTTP SERA ESSA.
				return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
				
			}
		}
	 
		
		/**
		 * Atualiza so campos especificos
		 * @param restauranteId
		 * @param campos
		 * @return
		 */
		@PatchMapping("/{restauranteId}") 
		public ResponseEntity<?> atualizarparcial(@PathVariable Long restauranteId , @RequestBody Map<String, Object> campos){
			
			Restaurante restauranteAtual = restauranteRepository.buscarPorId(restauranteId);
			if(restauranteAtual == null) {
				return ResponseEntity.notFound().build();
			}
			
			// Atribui os valores do campos para dentro do restauranteAtual
			merge(campos , restauranteAtual);
			
			 return atualizar(restauranteId, restauranteAtual);
		}


		/**
		 * Metodo que mescla os valores do postman com os valores armazendos no Banco de dados, atualizando assim
		 * somente campos especificos
			 * Expressao Lambda
			 * nomePropriedade -> String do Map
			 * valorPropriedade -> Object do Map
			 * REFLECTIONS -> INSPECIONA OBJETOS JAVA EM TEMPO DE EXECUÇÃO DE FORMA DINAMICA
		 * @param camposOrigem
		 * @param restauranteDestino
		 */
		private void merge(Map<String, Object> dadosOrigem , Restaurante restauranteDestino) {
			
			// Serializa e converte objetos java em JSON e ao contrario tb
			ObjectMapper objectMapper = new ObjectMapper();
			// Criando um objeto Restaurante com os dados da origem(POSTMAN), JA CONVERTIDO E EVITANDO ERROS
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class); 
			
			System.out.println(restauranteOrigem);
			
			// Pegando as propriedades passadas pelo cliente
			dadosOrigem.forEach((nomePropriedade , valorPropriedade) -> {
				
				// field -> representa um atributo da classe Restaurante
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade); // Retorna a instancia de um campo
				
				field.setAccessible(true); // Acessa variavel private
				
				 // Buscando o valor da propriedade representada pelo field, dentro da instancia de restauranteOrigem
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
				
				System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
				
				// field -> atribuindo o valorPropriedade de instancia que é o nome, no nome da do restauranteDestino(Restaurante "nome") 
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

}
