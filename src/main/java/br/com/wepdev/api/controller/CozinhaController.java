package br.com.wepdev.api.controller;

import java.util.List;
import java.util.Optional;

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
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;
import br.com.wepdev.domain.service.CozinhaService;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/cozinhas") //, produces = MediaType.APPLICATION_JSON_VALUE) // Toda a classe produz JSON
public class CozinhaController {
	
	
	@Autowired
	private CozinhaRepository CozinhaRepository;
	
	@Autowired
	private CozinhaService cozinhaService;
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Produz Json - Anotacao colocada no escopo da classe
	public List<Cozinha> listar(){
		return CozinhaRepository.findAll();
	}
	

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscarPorId(@PathVariable Long cozinhaId) {
		Optional<Cozinha> cozinha = CozinhaRepository.findById(cozinhaId); // O findById nunca returna um null. e sim um Optional
		
		if(cozinha.isPresent()) { // Como agora o retorno e um Optional, no if ele pergunta se tem algo dentro com isPresent()
			return ResponseEntity.ok(cozinha.get()); // Pegando a instancia da cozinha com get() que esta dentro do Optional
		}
		//return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Se nao existir o Id retorna um NOT FOUND e sem corpo.
		return ResponseEntity.notFound().build(); // Atalho para a linha acima
	}
	
	
	/**
	 * @RequestBody Cozinha -> o RequestBody vai receber o corpo da Cozinha
	 * @param cozinha
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED) // Status 201, recurso criado
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return cozinhaService.salvarOuAtualiza(cozinha); // Retona a cozinha no corpo
	}
	
	
	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, 
			@RequestBody Cozinha cozinha){
		Optional<Cozinha> cozinhaAtual = CozinhaRepository.findById(cozinhaId); // Cozinha armazenada no Banco de dados
		//cozinhaAtual.setNome(cozinha.getNome()); // Setando o nome da cozinha que veio da requisicao e colocando na cozinha armazenada no banco
		
		if(cozinhaAtual.isPresent()) {
			/*
			 * Faz a mesma coisa que a linha acima, seta todas as propriedades de uma unica vez.
			 * NO TERCEIRO PARAMETRO O CAMPO Id É IGNORADO, EVITANDO ERROS , POIS O ID NAO DEVE SER MODIFICADO PQ ESTA SENDO FEITA UMA ATUALIZAÇÃO
			 */
			BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");  // Pegando a instancia da cozinhaAtual com get() que esta dentro do Optional
			
			Cozinha cozinhaSalva = cozinhaService.salvarOuAtualiza(cozinhaAtual.get());
			return ResponseEntity.ok(cozinhaSalva);
	    }
		return ResponseEntity.notFound().build(); // Se nao existir o Id da cozinha retorna um NOT FOUND e sem corpo.
	}
	
	
	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<?> remover(@PathVariable Long cozinhaId){ // Foi colocado o coringa <?> para que no corpo da resposta possa ser passado qualquer tipo de objeto
		try {
			 cozinhaService.excluir(cozinhaId);
			 return ResponseEntity.noContent().build(); // Como o recurso ja foi removido na ha necessidade de retornar um corpo
			 
		} catch (EntidadeNaoEncontradaException e) { // Excessao de negocio customizada
			//AO TENTAR REMOVER UMA COZINHA VINCULADA AO RESTAURANTE OCORRE UM ERRO DE VIOLAÇÃO NO BD, RESPOSTA HTTP SERA ESSA.
			return ResponseEntity.notFound().build(); // Como o recurso ja foi removido na ha necessidade de retornar um corpo 
			
		} catch (EntidadeEmUsoException e) { // Excessao de negocio customizada
			//AO TENTAR REMOVER UMA COZINHA VINCULADA AO RESTAURANTE OCORRE UM ERRO DE VIOLAÇÃO NO BD, RESPOSTA HTTP SERA ESSA.
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // Passando a mensagem no corpo do Objeto EntidadeEmUsoException
			
		}
	}
	

	
	

}
