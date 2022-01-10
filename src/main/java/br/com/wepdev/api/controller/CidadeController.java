package br.com.wepdev.api.controller;

import java.util.List;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.api.exceptionhandler.Problem;
import br.com.wepdev.api.inputDTO.CidadeInputDTO;
import br.com.wepdev.api.converter.CidadeConverterDTO;
import br.com.wepdev.api.converter.CidadeInputConverterCidade;
import br.com.wepdev.domain.exception.EstadoNaoEncontradoException;
import br.com.wepdev.domain.exception.NegocioException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.repository.CidadeRepository;
import br.com.wepdev.domain.service.CidadeService;

import javax.validation.Valid;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@Api(tags = "Cidades") // Controlador com recursos para utilização do swagger
@RestController // Substitue as 2 anotacoes acima, Essa classe e a responsavel pelas respostas HTTP
@RequestMapping(value = "/cidades")
public class CidadeController {
	
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private CidadeConverterDTO cidadeConverterDTO;

	@Autowired
	private CidadeInputConverterCidade cidadeInputConverterCidade;

	

	@ApiOperation("Lista as cidades")
	@GetMapping
	public List<CidadeDTO> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();

		return cidadeConverterDTO.converteListaEntidadeParaListaDto(todasCidades);

	}


	/**
	 * example = "1" -> Parametro que ja preenche na pagina HTML de documentação do swagger o numero 1 como exemplo do que deve ser preenchido.
	 * @param cidadeId
	 * @return
	 */
	@ApiOperation("Busca uma cidade por ID")
	@ApiResponses({ // Adicionando respostas de erro na documentação com a representação no payload da resposta
			@ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(schema = @Schema(implementation = Problem.class))),
			@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))

	})
	@GetMapping("/{cidadeId}")
	public CidadeDTO buscar(@ApiParam(value = "ID de uma cidade", example = "1") @PathVariable Long cidadeId) {
		Cidade cidade = cidadeService.buscarOuFalhar(cidadeId);
	    return cidadeConverterDTO.converteEntidadeParaDto(cidade);
	}


	@ApiOperation("Cadastra uma cidade")
	@ApiResponses({ // Adicionando respostas de erro na documentação com a representação no payload da resposta
			@ApiResponse(responseCode = "201", description = "Cidade cadastrada")
	})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeDTO adicionar(@ApiParam(name = "corpo", value = "Representação de uma nova cidade")
								   @RequestBody @Valid CidadeInputDTO cidadeInput) {
		try {
			Cidade cidade = cidadeInputConverterCidade.converteInputParaEntidade(cidadeInput);
			cidade = cidadeService.salvar(cidade);

			return cidadeConverterDTO.converteEntidadeParaDto(cidade);
		}catch (EstadoNaoEncontradoException e){
			throw new NegocioException(e.getMessage(), e);
		}

	}


	@ApiOperation("Atualiza uma cidade por ID")
	@ApiResponses({ // Adicionando respostas de erro na documentação com a representação no payload da resposta
			@ApiResponse(responseCode = "200", description = "Cidade atualizada"),
			@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
	})
	@PutMapping("/{cidadeId}")
	public CidadeDTO atualizar(
			@ApiParam(value = "ID de uma cidade")
			@PathVariable Long cidadeId,
			@ApiParam(name = "corpo", value = "Representação de uma cidade com os novos dados")
			@RequestBody @Valid CidadeInputDTO cidadeInput) {

			try{
				Cidade cidadeAtual = cidadeService.buscarOuFalhar(cidadeId);

				cidadeInputConverterCidade.copiaInputParaEntidade(cidadeInput, cidadeAtual);
				cidadeAtual = cidadeService.salvar(cidadeAtual);

				return cidadeConverterDTO.converteEntidadeParaDto(cidadeAtual);

			}catch (EstadoNaoEncontradoException e){
                throw new NegocioException(e.getMessage(), e);
			}
	}


	@ApiOperation("Exclui uma cidade por ID")
	@ApiResponses({ // Adicionando respostas de erro na documentação com a representação no payload da resposta
			@ApiResponse(responseCode = "204", description = "Cidade excluída", content = @Content(schema = @Schema(implementation = Problem.class))),
			@ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
	})
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@ApiParam(value = "ID de uma cidade") @PathVariable Long cidadeId) {

			  cidadeService.excluir(cidadeId);
	}






}
