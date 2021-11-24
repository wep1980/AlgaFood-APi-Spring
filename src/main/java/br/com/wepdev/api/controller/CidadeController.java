package br.com.wepdev.api.controller;

import java.util.List;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.api.inputDTO.CidadeInputDTO;
import br.com.wepdev.api.converter.CidadeConverterDTO;
import br.com.wepdev.api.converter.CidadeInputConverterCidade;
import br.com.wepdev.domain.exception.EstadoNaoEncontradoException;
import br.com.wepdev.domain.exception.NegocioException;
import io.swagger.annotations.Api;
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

	
	
	@GetMapping
	public List<CidadeDTO> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();
		return cidadeConverterDTO.converteListaEntidadeParaListaDto(todasCidades);
	}


	@GetMapping("/{cidadeId}")
	public CidadeDTO buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cidadeService.buscarOuFalhar(cidadeId);
	    return cidadeConverterDTO.converteEntidadeParaDto(cidade);
	}


	@PostMapping
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


	@PutMapping("/{cidadeId}")
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
	public void remover(@PathVariable Long cidadeId) {
			  cidadeService.excluir(cidadeId);
	}






}
