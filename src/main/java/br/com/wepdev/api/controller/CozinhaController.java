package br.com.wepdev.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	/**
	 * ResponseEntity possui um metodo status, que define o status da resposta que sera devolvido e devolvendo um corpo
	 * que nesse caso e a cozinha
	 * @param cozinhaId
	 * @return
	 */
	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscarPorId(@PathVariable Long cozinhaId) {
		Cozinha cozinha = repository.buscarPorId(cozinhaId);
		
		//return ResponseEntity.status(HttpStatus.OK).body(cozinha); // Envia na resposta o status e um corpo, que nesse caso e a cozinha
		//return ResponseEntity.ok(cozinha); // Resposta mais simples
		//return ResponseEntity.status(HttpStatus.OK).build(); // resposta com o status mas sem o corpo.
		
		//------- RESPOSTA DE STATUS COM REDIRECIONAMENTO
		
		// Criando o headers para ir na resposta
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.LOCATION, "http://localhost:8080/cozinhas"); // informa a uri temporaria
		
		return ResponseEntity
				.status(HttpStatus.FOUND)
				.headers(headers).build(); // Resposta de redirecionamento de uri temporario
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
