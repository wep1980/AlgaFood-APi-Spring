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
import org.springframework.web.bind.annotation.RestController;

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
	
	
	
	
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Produz Json - Anotacao colocada no escopo da classe
		public List<Cidade> listar() {
			return cidadeRepository.listar();
		}
    
    
	@GetMapping("/{cidadeId}")
	public ResponseEntity<Cidade> buscarPorId(@PathVariable Long cidadeId) {
		Cidade cidade = cidadeRepository.buscarPorId(cidadeId);
		
		if(cidade != null) {
			return ResponseEntity.ok(cidade);
		}
		//return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Se nao existir o Id retorna um NOT FOUND e sem corpo.
		return ResponseEntity.notFound().build(); // Atalho para a linha acima
	}
	
	
	
	/**
	 * ResponseEntity<?> salvar -> <?> Foi colocado um coringa para que a resposta no corpo fosse de qualquer tipo, 
	 * nao so de cidade, ja que a resposta deve ser uma string pois uma message de erro sera enviada atraves do getMessage() caso aconteça
	 * @param restaurante
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody Cidade cidade){
		try {
			cidade = cidadeService.salvar(cidade);
			return ResponseEntity.status(HttpStatus.CREATED).body(cidade);
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	
	/**
	 * ResponseEntity<?> atualizar -> <?> Foi colocado um coringa para que a resposta no corpo fosse de qualquer tipo, 
	 * nao so de cidade, ja que a resposta deve ser uma string pois uma message de erro sera enviada atraves do getMessage() caso aconteça
	 * @param restauranteId
	 * @param cidade
	 * @return
	 */
    @PutMapping("/{cidadeId}")
    public ResponseEntity<?> atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
        try {
			Cidade cidadeAtual = cidadeRepository.buscarPorId(cidadeId);
			
			if (cidadeAtual != null) {
				BeanUtils.copyProperties(cidade, cidadeAtual, "id");
				
				cidadeAtual = cidadeService.salvar(cidadeAtual);
				return ResponseEntity.ok(cidadeAtual);
			}
			return ResponseEntity.notFound().build();
		
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());
		}
    }
	
	
	
	@DeleteMapping("/{cidadeId}")
	public ResponseEntity<Cidade> remover(@PathVariable Long cidadeId){
		try {
			 cidadeService.remover(cidadeId);
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
