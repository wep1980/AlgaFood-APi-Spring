package br.com.wepdev.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	
	
	@GetMapping
	public List<Estado> listar() {
		return estadoRepository.findAll();
	}


	@GetMapping("/{estadoId}")
	public Estado buscar(@PathVariable Long estadoId) {
		return estadoService.buscarOuFalhar(estadoId);
	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Estado adicionar(@RequestBody Estado estado) {
		return estadoService.salvar(estado);
	}


	@PutMapping("/{estadoId}")
	public Estado atualizar(@PathVariable Long estadoId, @RequestBody Estado estado) {
		    //Busca o estado atual ou lança uma exception que esta com NOT.FOUND
		    Estado estadoAtual = estadoService.buscarOuFalhar(estadoId);
			// Copia a instancia de estado para estadoAtual, exceto o id
			BeanUtils.copyProperties(estado, estadoAtual, "id");
		    // Salva e retorna o corpo, e a resposta HTTP e enviada como 200 -> OK
			return estadoService.salvar(estadoAtual);
	}


	@DeleteMapping("/{estadoId}")
	public void remover(@PathVariable Long estadoId) {
		estadoService.excluir(estadoId);
	}
	
	
	
}
