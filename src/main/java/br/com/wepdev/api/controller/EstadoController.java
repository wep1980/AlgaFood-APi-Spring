package br.com.wepdev.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.model.Estado;
import br.com.wepdev.domain.repository.EstadoRepository;
import br.com.wepdev.domain.service.EstadoService;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/estados")
public class EstadoController {
	
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private EstadoService estadoService;
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Produz Json - Anotacao colocada no escopo da classe
	public List<Estado> listar(){
		return estadoRepository.listar();
	}
	

	@GetMapping("/{estadoId}")
	public ResponseEntity<Estado> buscarPorId(@PathVariable Long estadoId) {
		Estado estado = estadoRepository.buscarPorId(estadoId);
		
		if(estado != null) {
			return ResponseEntity.ok(estado);
		}
		//return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Se nao existir o Id retorna um NOT FOUND e sem corpo.
		return ResponseEntity.notFound().build(); // Atalho para a linha acima
	}
	
	
	/**
	 * @RequestBody Cozinha -> o RequestBody vai receber o corpo da Cozinha
	 * @param cozinha
	 */
	  @PostMapping
		@ResponseStatus(HttpStatus.CREATED)
		public Estado adicionar(@RequestBody Estado estado) {
			return estadoService.salvar(estado);
		}
	
	
	    @PutMapping("/{estadoId}")
		public ResponseEntity<Estado> atualizar(@PathVariable Long estadoId, @RequestBody Estado estado) {
			Estado estadoAtual = estadoRepository.buscarPorId(estadoId);
			
			if (estadoAtual != null) {
				BeanUtils.copyProperties(estado, estadoAtual, "id");
				
				estadoAtual = estadoService.salvar(estadoAtual);
				return ResponseEntity.ok(estadoAtual);
			}
			
			return ResponseEntity.notFound().build();
		}
	
	
	@DeleteMapping("/{estadoId}")
	public ResponseEntity<Estado> remover(@PathVariable Long estadoId){
		try {
			 estadoService.remover(estadoId);
			 return ResponseEntity.noContent().build(); // Como o recurso ja foi removido na ha necessidade de retornar um corpo
			 
		} catch (EntidadeNaoEncontradaException e) { // Excessao de negocio customizada
			//AO TENTAR REMOVER UMA COZINHA VINCULADA AO RESTAURANTE OCORRE UM ERRO DE VIOLAÇÃO NO BD, RESPOSTA HTTP SERA ESSA.
			return ResponseEntity.noContent().build(); // Como o recurso ja foi removido na ha necessidade de retornar um corpo 
			
		} catch (EntidadeEmUsoException e) { // Excessao de negocio customizada
			//AO TENTAR REMOVER UMA COZINHA VINCULADA AO RESTAURANTE OCORRE UM ERRO DE VIOLAÇÃO NO BD, RESPOSTA HTTP SERA ESSA.
			return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
		}
	}
	
	
	
}
