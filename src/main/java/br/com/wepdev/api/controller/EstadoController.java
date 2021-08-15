package br.com.wepdev.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wepdev.domain.model.Estado;
import br.com.wepdev.domain.repository.EstadoRepository;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/estados")
public class EstadoController {
	
	
	@Autowired
	private EstadoRepository repository;
	
	
	@GetMapping
	public List<Estado> listar(){
		return repository.listar();
	}

}
