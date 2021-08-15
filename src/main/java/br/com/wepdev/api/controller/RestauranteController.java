package br.com.wepdev.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
	private RestauranteRepository repository;

	
	
	    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Produz Json - Anotacao colocada no escopo da classe
		public List<Restaurante> listar() {
			return repository.listar();
		}
	    
	    
		@GetMapping("/{restauranteId}")
		public ResponseEntity<Restaurante> buscarPorId(@PathVariable Long restauranteId) {
			Restaurante restaurante = repository.buscarPorId(restauranteId);
			
			if(restaurante != null) {
				return ResponseEntity.ok(restaurante);
			}
			//return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Se nao existir o Id retorna um NOT FOUND e sem corpo.
			return ResponseEntity.notFound().build(); // Atalho para a linha acima
		}
		
		
		/**
		 * public ResponseEntity<?> salvarOuAtualizar -> <?> Foi colocado um coringa para que a resposta no corpo fosse de qualquer tipo, 
		 * nao so de restaurante, ja que a resposta deve ser uma string pois uma message de erro sera enviada atraves do getMessage() caso aconteça
		 * @param restaurante
		 * @return
		 */
		@PostMapping
		public ResponseEntity<?> salvarOuAtualizar(@RequestBody Restaurante restaurante){
			try {
				restaurante = restauranteService.salvarOuAtualizar(restaurante);
				return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
			} catch (EntidadeNaoEncontradaException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
	 

}
