package br.com.wepdev.api.controller;

import java.util.List;

import br.com.wepdev.api.DTO.CozinhaDTO;
import br.com.wepdev.api.DTO.INPUT.CozinhaInputDTO;
import br.com.wepdev.api.converter.CozinhaConverterDTO;
import br.com.wepdev.api.converter.CozinhaInputConverterCozinha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import javax.validation.Valid;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/cozinhas") //, produces = MediaType.APPLICATION_JSON_VALUE) // Toda a classe produz JSON
public class CozinhaController {
	
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CozinhaService cozinhaService;

	@Autowired
	private CozinhaConverterDTO cozinhaConverterDTO;

	@Autowired
	private CozinhaInputConverterCozinha cozinhaInputConverterCozinha;
	


	@GetMapping
	public List<CozinhaDTO> listar() {

		List<Cozinha> todasCozinha = cozinhaRepository.findAll();

		return cozinhaConverterDTO.converteListaEntidadeParaListaDto(todasCozinha);
	}



	@GetMapping("/{cozinhaId}")
	public CozinhaDTO buscar(@PathVariable Long cozinhaId) {

		Cozinha cozinha = cozinhaService.buscarOuFalhar(cozinhaId);

		return cozinhaConverterDTO.converteEntidadeParaDto(cozinha);
	}


	/**
	 * @Valid -> Valida cozinha na chamada do metodo
	 * @param cozinhaInput
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaDTO adicionar(@RequestBody @Valid CozinhaInputDTO cozinhaInput) {

		Cozinha cozinha = cozinhaInputConverterCozinha.converteInputParaEntidade(cozinhaInput);

		cozinha = cozinhaService.salvar(cozinha);

		return cozinhaConverterDTO.converteEntidadeParaDto(cozinha);
	}

	/**
	 *
	 * @param cozinhaId
	 * @param cozinhaInput
	 * @return
	 */
	@PutMapping("/{cozinhaId}")
	public CozinhaDTO atualizar(@PathVariable  Long cozinhaId, @RequestBody @Valid CozinhaInputDTO cozinhaInput) {

		Cozinha cozinhaAtual = cozinhaService.buscarOuFalhar(cozinhaId);

		 cozinhaInputConverterCozinha.copiaInputParaEntidade(cozinhaInput, cozinhaAtual);
		 cozinhaAtual = cozinhaService.salvar(cozinhaAtual);

		 return cozinhaConverterDTO.converteEntidadeParaDto(cozinhaAtual);
	}



	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // Em caso de sucesso manda um status no content
	public void remover(@PathVariable Long cozinhaId) {
		cozinhaService.excluir(cozinhaId);
	}

}
