package br.com.wepdev.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import br.com.wepdev.api.model.CozinhasXmlWrapper;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/cozinhas") //, produces = MediaType.APPLICATION_JSON_VALUE) // Toda a classe produz JSON
public class CozinhaController {
	
	
	@Autowired
	private CozinhaRepository repository;
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Produz Json - Anotacao colocada no escopo da classe
	public List<Cozinha> listar(){
		return repository.listar();
	}
	
	/**
	 * Metodo que vau retornar uma lista de cozinhas em formato XML customizado atraves da classe CozinhasXmlWrapper(VISUALIZACAO NO POSTMAN)
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE) // Produz XML 
	public CozinhasXmlWrapper listarXml(){
	   return new CozinhasXmlWrapper(repository.listar());
	}
	

	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscarPorId(@PathVariable Long cozinhaId) {
		Cozinha cozinha = repository.buscarPorId(cozinhaId);
		
		if(cozinha != null) {
			return ResponseEntity.ok(cozinha);
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
		return repository.salvarOuAtualizar(cozinha); // Retona a cozinha no corpo
	}
	
	
	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId, 
			@RequestBody Cozinha cozinha){
		Cozinha cozinhaAtual = repository.buscarPorId(cozinhaId); // Cozinha armazenada no Banco de dados
		//cozinhaAtual.setNome(cozinha.getNome()); // Setando o nome da cozinha que veio da requisicao e colocando na cozinha armazenada no banco
		
		if(cozinhaAtual != null) {
			/*
			 * Faz a mesma coisa que a linha acima, seta todas as propriedades de uma unica vez.
			 * NO TERCEIRO PARAMETRO O CAMPO Id É IGNORADO, EVITANDO ERROS , POIS O ID NAO DEVE SER MODIFICADO PQ ESTA SENDO FEITA UMA ATUALIZAÇÃO
			 */
			BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
			
			cozinhaAtual = repository.salvarOuAtualizar(cozinha);
			return ResponseEntity.ok(cozinhaAtual);
	    }
		return ResponseEntity.notFound().build(); // Se nao existir o Id da cozinha retorna um NOT FOUND e sem corpo.
	}
	
	
	@DeleteMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId){
		try {
		Cozinha cozinha = repository.buscarPorId(cozinhaId); // Cozinha armazenada no Banco de dados
		if(cozinha != null) {
			repository.remover(cozinha);
			
			return ResponseEntity.noContent().build(); // Como o recurso ja foi removido na ha necessidade de retornar um corpo
		}
		return ResponseEntity.notFound().build(); // Se nao existir o Id da cozinha retorna um NOT FOUND e sem corpo.
		
		} catch (DataIntegrityViolationException e) {
			//AO TENTAR REMOVER UMA COZINHA VINCULADA AO RESTAURANTE OCORRE UM ERRO DE VIOLAÇÃO NO BD, RESPOSTA HTTP SERA ESSA.
			return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
