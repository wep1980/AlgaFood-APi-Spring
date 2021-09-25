package br.com.wepdev.api.controller;

import java.util.List;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.api.DTO.INPUT.CidadeINPUT;
import br.com.wepdev.api.converter.CidadeConverterDTO;
import br.com.wepdev.api.converter.CidadeInputConverterCidade;
import br.com.wepdev.api.converter.EstadoConverterDTO;
import br.com.wepdev.api.converter.EstadoInputConverterEstado;
import br.com.wepdev.domain.exception.EstadoNaoEncontradoException;
import br.com.wepdev.domain.exception.NegocioException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.repository.CidadeRepository;
import br.com.wepdev.domain.service.CidadeService;

import javax.validation.Valid;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
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
		return cidadeConverterDTO.toCollectionModel(todasCidades);
	}


	@GetMapping("/{cidadeId}")
	public CidadeDTO buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cidadeService.buscarOuFalhar(cidadeId);
	    return cidadeConverterDTO.toModel(cidade);
	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeDTO adicionar(@RequestBody @Valid CidadeINPUT cidadeInput) {
		try {
			Cidade cidade = cidadeInputConverterCidade.toDomainObject(cidadeInput);
			cidade = cidadeService.salvar(cidade);

			return cidadeConverterDTO.toModel(cidade);
		}catch (EstadoNaoEncontradoException e){
			throw new NegocioException(e.getMessage(), e);
		}

	}


	@PutMapping("/{cidadeId}")
	public CidadeDTO atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeINPUT cidadeInput) {
			try{
				Cidade cidadeAtual = cidadeService.buscarOuFalhar(cidadeId);

				cidadeInputConverterCidade.copyToDomainObject(cidadeInput, cidadeAtual);
				cidadeAtual = cidadeService.salvar(cidadeAtual);

				return cidadeConverterDTO.toModel(cidadeAtual);

			}catch (EstadoNaoEncontradoException e){
                throw new NegocioException(e.getMessage(), e);
			}

	}

	@DeleteMapping("/{cidadeId}")
	public void remover(@PathVariable Long cidadeId) {
			  cidadeService.excluir(cidadeId);
	}






}
