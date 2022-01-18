package br.com.wepdev.api.controller;

import java.util.List;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.api.openapi.controller.CidadeControllerOpenApi;
import br.com.wepdev.api.inputDTO.CidadeInputDTO;
import br.com.wepdev.api.converter.CidadeConverterDTO;
import br.com.wepdev.api.converter.CidadeInputConverterCidade;
import br.com.wepdev.domain.exception.EstadoNaoEncontradoException;
import br.com.wepdev.domain.exception.NegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.repository.CidadeRepository;
import br.com.wepdev.domain.service.CidadeService;

import javax.validation.Valid;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
//@Api(tags = "Cidades") // Controlador com recursos para utilização do swagger
@RestController // Substitue as 2 anotacoes acima, Essa classe e a responsavel pelas respostas HTTP
@RequestMapping(value = "/cidades")
public class CidadeController implements CidadeControllerOpenApi {
	
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private CidadeConverterDTO cidadeConverterDTO;

	@Autowired
	private CidadeInputConverterCidade cidadeInputConverterCidade;

	

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CidadeDTO> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();

		return cidadeConverterDTO.converteListaEntidadeParaListaDto(todasCidades);

	}


	/**
	 * example = "1" -> Parametro que ja preenche na pagina HTML de documentação do swagger o numero 1 como exemplo do que deve ser preenchido.
	 * @param cidadeId
	 * @return
	 */
	@GetMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CidadeDTO buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cidadeService.buscarOuFalhar(cidadeId);
	    return cidadeConverterDTO.converteEntidadeParaDto(cidade);
	}


	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeDTO adicionar(@RequestBody @Valid CidadeInputDTO cidadeInput) {
		try {
			Cidade cidade = cidadeInputConverterCidade.converteInputParaEntidade(cidadeInput);
			cidade = cidadeService.salvar(cidade);

			return cidadeConverterDTO.converteEntidadeParaDto(cidade);
		}catch (EstadoNaoEncontradoException e){
			throw new NegocioException(e.getMessage(), e);
		}

	}



	@PutMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CidadeDTO atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInputDTO cidadeInput) {
			try{
				Cidade cidadeAtual = cidadeService.buscarOuFalhar(cidadeId);

				cidadeInputConverterCidade.copiaInputParaEntidade(cidadeInput, cidadeAtual);
				cidadeAtual = cidadeService.salvar(cidadeAtual);

				return cidadeConverterDTO.converteEntidadeParaDto(cidadeAtual);

			}catch (EstadoNaoEncontradoException e){
                throw new NegocioException(e.getMessage(), e);
			}
	}



	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {

			  cidadeService.excluir(cidadeId);
	}






}
