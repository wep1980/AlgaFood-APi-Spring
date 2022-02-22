package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.CozinhaDTO;
import br.com.wepdev.api.converter.CozinhaConverterDTO;
import br.com.wepdev.api.converter.CozinhaInputConverterCozinha;
import br.com.wepdev.api.inputDTO.CozinhaInputDTO;
import br.com.wepdev.api.openapi.controller.CozinhaControllerOpenApi;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;
import br.com.wepdev.domain.service.CozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/cozinhas") //, produces = MediaType.APPLICATION_JSON_VALUE) // Toda a classe produz JSON
public class CozinhaController implements CozinhaControllerOpenApi {
	
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CozinhaService cozinhaService;

	@Autowired
	private CozinhaConverterDTO cozinhaConverterDTO;

	@Autowired
	private CozinhaInputConverterCozinha cozinhaInputConverterCozinha;
	


//	@GetMapping
//	public List<CozinhaDTO> listar() {
//
//		List<Cozinha> todasCozinha = cozinhaRepository.findAll();
//
//		return cozinhaConverterDTO.converteListaEntidadeParaListaDto(todasCozinha);
//	}


	/**
	 * @PageableDefault(size = 10) -> Apenas elementos serão exibidos por pagina
	 * @param pageable
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<CozinhaDTO> listarComPaginacao(@PageableDefault(size = 10) Pageable pageable) {

		Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable); // O findAll() ja possui a sobrecarga para o Page

		List<CozinhaDTO> cozinhasDto = cozinhaConverterDTO.converteListaEntidadeParaListaDto(cozinhasPage.getContent());

		/**
		 * Colocando uma lista de CozinhaDTO dentro do Page.
		 * cozinhasDto -> conteudo que sera colocado dentro do page.
		 * pageable -> parametro recebido no endpoint.
		 * cozinhasPage.getTotalElements() -> pegando o total de elementos de dentro do cozinhasPage
		 *
		 * Como o retorno e um Page, na representacao tera todos os elementos da paginação
		 */
		Page<CozinhaDTO> cozinhasDtoPage = new PageImpl<>(cozinhasDto, pageable, cozinhasPage.getTotalElements());

		return cozinhasDtoPage;
	}



	@GetMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaDTO buscar(@PathVariable Long cozinhaId) {

		Cozinha cozinha = cozinhaService.buscarOuFalhar(cozinhaId);

		return cozinhaConverterDTO.converteEntidadeParaDto(cozinha);
	}


	/**
	 * @Valid -> Valida cozinha na chamada do metodo
	 * @param cozinhaInput
	 * @return
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
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
	@PutMapping(path = "/{cozinhaId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CozinhaDTO atualizar(@PathVariable  Long cozinhaId, @RequestBody @Valid CozinhaInputDTO cozinhaInput) {

		Cozinha cozinhaAtual = cozinhaService.buscarOuFalhar(cozinhaId);

		 cozinhaInputConverterCozinha.copiaInputParaEntidade(cozinhaInput, cozinhaAtual);
		 cozinhaAtual = cozinhaService.salvar(cozinhaAtual);

		 return cozinhaConverterDTO.converteEntidadeParaDto(cozinhaAtual);
	}



	@DeleteMapping(path = "/{cozinhaId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT) // Em caso de sucesso manda um status no content
	public void remover(@PathVariable Long cozinhaId) {

		cozinhaService.excluir(cozinhaId);
	}

}
