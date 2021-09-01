package br.com.wepdev.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.repository.CidadeRepository;
import br.com.wepdev.domain.service.CidadeService;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/cidades")
public class CidadeController {
	
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CidadeService cidadeService;

	
	
	@GetMapping
	public List<Cidade> listar() {
		return cidadeRepository.findAll();
	}


	@GetMapping("/{cidadeId}")
	public Cidade buscar(@PathVariable Long cidadeId) {
	    return cidadeService.buscarOuFalhar(cidadeId);
	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cidade adicionar(@RequestBody Cidade cidade) {
			return cidadeService.salvar(cidade);
	}


	@PutMapping("/{cidadeId}")
	public Cidade atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
		    //Busca a cidade atual ou lança uma exception que esta com NOT.FOUND
			Cidade cidadeAtual = cidadeService.buscarOuFalhar(cidadeId);
			// Copia a instancia de cidade para cidadeAtual, exceto o id
		    BeanUtils.copyProperties(cidade, cidadeAtual, "id");
		    // Salva e retorna o corpo, e a resposta HTTP e enviada como 200 -> OK
			return cidadeService.salvar(cidadeAtual);
	}


	@DeleteMapping("/{cidadeId}")
	public void remover(@PathVariable Long cidadeId) {
			  cidadeService.excluir(cidadeId);
	}

}
