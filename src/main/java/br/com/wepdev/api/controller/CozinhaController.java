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
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;
import br.com.wepdev.domain.service.CozinhaService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/cozinhas") //, produces = MediaType.APPLICATION_JSON_VALUE) // Toda a classe produz JSON
public class CozinhaController {
	
	
	@Autowired
	private CozinhaRepository CozinhaRepository;
	
	@Autowired
	private CozinhaService cozinhaService;
	


	@GetMapping
	public List<Cozinha> listar() {
		return CozinhaRepository.findAll();
	}
	
	@GetMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
		Optional<Cozinha> cozinha = CozinhaRepository.findById(cozinhaId);
		
		if (cozinha.isPresent()) {
			return ResponseEntity.ok(cozinha.get());
		}
		
		return ResponseEntity.notFound().build();
	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cozinha adicionar(@RequestBody Cozinha cozinha) {
		return cozinhaService.salvar(cozinha);
	}
	
	@PutMapping("/{cozinhaId}")
	public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId,
			@RequestBody Cozinha cozinha) {
		Optional<Cozinha> cozinhaAtual = CozinhaRepository.findById(cozinhaId);
		
		if (cozinhaAtual.isPresent()) {
			BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");
			
			Cozinha cozinhaSalva = cozinhaService.salvar(cozinhaAtual.get());
			return ResponseEntity.ok(cozinhaSalva);
		}
		
		return ResponseEntity.notFound().build();
	}


//	@DeleteMapping("/{cozinhaId}")
//	public ResponseEntity<?> remover(@PathVariable Long cozinhaId) {
//		try {
//			cozinhaService.excluir(cozinhaId);
//			return ResponseEntity.noContent().build();
//
//		} catch (EntidadeNaoEncontradaException e) {
//			//return ResponseEntity.notFound().build(); // O NOTFOUND nao permite customizar o corpo
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não encontrado"); // O Body permite retornar um responsyEntity no corpo
//
//		} catch (EntidadeEmUsoException e) {
//			return ResponseEntity.status(HttpStatus.CONFLICT)
//					.body(e.getMessage());
//		}
//	}


	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // Em caso de sucesso manda um status no content
	public void remover(@PathVariable Long cozinhaId) {
		cozinhaService.excluir(cozinhaId);
		// Nao é mais necessario colocar o try catch aqui pois a excessao esta sendo tratado dentro do serviceCozinha, o que nao é uma boa pratica
//		try {
//			cozinhaService.excluir(cozinhaId);
//		} catch (EntidadeNaoEncontradaException e){
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage()); // Dessa forma ainda n é possivel customizar totalmente a resposta nas excessoes
//			//throw new ServerWebInputException(e.getMessage()); // Retorna o status e a mensagem, a mesma coisa que o codigo acima.
//
//		}

	}

}
