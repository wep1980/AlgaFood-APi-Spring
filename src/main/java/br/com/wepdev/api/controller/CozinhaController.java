package br.com.wepdev.api.controller;

import java.util.List;
import java.util.Optional;

import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
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

import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;
import br.com.wepdev.domain.service.CozinhaService;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/cozinhas") //, produces = MediaType.APPLICATION_JSON_VALUE) // Toda a classe produz JSON
public class CozinhaController {
	
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CozinhaService cozinhaService;
	


	@GetMapping
	public List<Cozinha> listar() {
		return cozinhaRepository.findAll();
	}



	@GetMapping("/{cozinhaId}")
	public Cozinha buscar(@PathVariable Long cozinhaId) {

		return cozinhaService.buscarOuFalhar(cozinhaId);
	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return cozinhaService.salvar(cozinha);
	}

	/**
	 *
	 * @param cozinhaId
	 * @param cozinha
	 * @return
	 */
	@PutMapping("/{cozinhaId}")
	public Cozinha atualizar(@PathVariable Long cozinhaId, @RequestBody Cozinha cozinha) {
		//Busca a cozinha atual ou lança uma exception que esta com NOT.FOUND
		Cozinha cozinhaAtual = cozinhaService.buscarOuFalhar(cozinhaId);
            // Copia a instancia de cozinha para cozinhaAtual, exceto o id
			BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
			// Salva e retorna o corpo, e a resposta HTTP e enviada como 200 -> OK
			return cozinhaService.salvar(cozinhaAtual);
	}



	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // Em caso de sucesso manda um status no content
	public void remover(@PathVariable Long cozinhaId) {
		cozinhaService.excluir(cozinhaId);
	}

}
