package br.com.wepdev.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {
	
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	
	/**
	 * O nome nao sera passado na Uri com @PathVariable e sim por @RequestParam (query Strings), exemplo abaixo.
	 * localhost:8080/teste/cozinhas/por-nome?nome=Brasileira 
	 * @param nome
	 * @return
	 */
//	@GetMapping("/cozinha/por-nome") 
//	public List<Cozinha> cozinhasPorNome(@RequestParam("nome") String nome){ // Fazendo binding do nome que vem da requisicao para a variavel nome
//		return cozinhaRepository.consultarPorNome(nome);
//	}

}
