package br.com.wepdev.api.controller;

import java.util.List;

import br.com.wepdev.api.DTO.INPUT.RestauranteInputDTO;
import br.com.wepdev.api.DTO.RestauranteDTO;
import br.com.wepdev.api.converter.RestauranteInputConverterRestaurante;
import br.com.wepdev.api.converter.RestauranteConverterDTO;
import br.com.wepdev.domain.exception.CidadeNaoEncontradaException;
import br.com.wepdev.domain.exception.CozinhaNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.RestauranteRepository;
import br.com.wepdev.domain.service.RestauranteService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired 
	private RestauranteRepository restauranteRepository;

	@Autowired
	private RestauranteConverterDTO restauranteConverterDTO;

	@Autowired
	private RestauranteInputConverterRestaurante restInputConverterRestaurante;

	/*
	SmartValidator -> Api do beanValidation, recebe uma instancia para validação
	 */
	@Autowired
	private SmartValidator validato;

	
	
	@GetMapping
	public List<RestauranteDTO> listar() {
		return restauranteConverterDTO.converteListaEntidadeParaListaDto(restauranteRepository.findAll());
	}

	
	@GetMapping("/{restauranteId}")
	public RestauranteDTO buscar(@PathVariable Long restauranteId) {

		Restaurante  restaurante = restauranteService.buscarOuFalhar(restauranteId);

		return restauranteConverterDTO.converteEntidadeParaDto(restaurante);
	}


	/**
	 * @Valid -> a validação da instancia de Restaurante e feita antes de o metodo ser chamado, ele sempre valida as propriedades que possuem
	 * as anotações para validações e essas anotações por padrão fazem parte de um grupo default, exemplo : @NotBlank(groups = Default.class)
	 * @Validated(Grupos.CadastroRestaurante.class) -> Valida Restaurante todas as propriedades que possuem o groups CadastroRestaurante.class,
	 * é um grupo criado para validar somente as propriedades que fazem parte desse grupo (Grupos.CadastroRestaurante.class).
	 * @param restauranteInputDTO
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteDTO adicionar(@RequestBody @Valid RestauranteInputDTO restauranteInputDTO) {

		try {
			Restaurante restaurante = restInputConverterRestaurante.converteInputParaEntidade(restauranteInputDTO);

			return restauranteConverterDTO.converteEntidadeParaDto(restauranteService.salvar(restaurante));

		}catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}



	@PutMapping("/{restauranteId}")
	public RestauranteDTO atualizar(@PathVariable Long restauranteId, @RequestBody @Valid RestauranteInputDTO restauranteInputDTO) {
		try {
			Restaurante restauranteAtual = restauranteService.buscarOuFalhar(restauranteId);

			restInputConverterRestaurante.copiaInputParaEntidade(restauranteInputDTO, restauranteAtual);

			return restauranteConverterDTO.converteEntidadeParaDto(restauranteService.salvar(restauranteAtual));

		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e){
			throw new NegocioException(e.getMessage(), e);
		}
	}


	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId){
		restauranteService.ativar(restauranteId);

	}


	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId){
		restauranteService.inativar(restauranteId);

	}


	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {

		restauranteService.abrir(restauranteId);
	}

	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {

		restauranteService.fechar(restauranteId);
	}
}
