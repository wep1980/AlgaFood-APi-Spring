package br.com.wepdev.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
//	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE) // Produz Json - Anotacao colocada no escopo da classe
//	public List<Cozinha> listar(){
//		return repository.listar();
//	}

}
