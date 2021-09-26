package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.FormaPagamentoDTO;
import br.com.wepdev.api.converter.FormaPagamentoConverterDTO;
import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController {



	@Autowired
	private RestauranteService restauranteService;

	@Autowired
	private FormaPagamentoConverterDTO formaPagamentoConverterDTO;

	
	
	@GetMapping
	public List<FormaPagamentoDTO> listarFormasPagamentoRestaurante(@PathVariable Long restauranteId) {

          Restaurante restaurante = restauranteService.buscarOuFalhar(restauranteId);
		  return formaPagamentoConverterDTO.converteListaEntidadeParaListaDto(restaurante.getFormasPagamento());
	}


	/**
	 *
	 * @param restauranteId ta fazendo binding com @RequestMapping declarado na Classe no inicio dela
	 * @param formaPagamentoId
	 */
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // Retorna o status no content
	public void desassociarFormaPagamentoDoRestaurante(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId){

		restauranteService.desassociarFormaPagamento(restauranteId, formaPagamentoId);
	}


	/**
	 *
	 * @param restauranteId ta fazendo binding com @RequestMapping declarado na Classe no inicio dela
	 * @param formaPagamentoId
	 */
	@PutMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // Retorna o status no content
	public void associarFormaPagamentoDoRestaurante(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId){

		restauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);
	}

}
