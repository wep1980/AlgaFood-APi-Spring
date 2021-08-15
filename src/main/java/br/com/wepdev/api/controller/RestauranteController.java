package br.com.wepdev.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		 * Atualiza so campos selecionados, utilizando Map conseguimos usar String(chave) como nome dos campos e o valor como um objeto qualquer
		 * @param restauranteId
		 * @param campos
		 * @return
		 */
		@PatchMapping("/{restauranteId}") 
		public ResponseEntity<?> atualizarparcial(@PathVariable Long restauranteId , @RequestBody Map<String, Object> campos){
			/*
			 * Expressao Lambda
			 * nomePropriedade -> String do Map
			 * valorPropriedade -> Object do Map
			 */
			campos.forEach((nomePropriedade , valorPropriedade) -> {
				System.out.println(nomePropriedade + " - " + valorPropriedade);
			});
			 return ResponseEntity.ok().build();
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

}
