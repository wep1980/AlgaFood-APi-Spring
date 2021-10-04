package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.INPUT.PedidoInputDTO;
import br.com.wepdev.api.DTO.PedidoDTO;
import br.com.wepdev.api.DTO.PedidoResumoDTO;
import br.com.wepdev.api.converter.PedidoConverterDTO;
import br.com.wepdev.api.converter.PedidoInputConverterPedido;
import br.com.wepdev.api.converter.PedidoResumoConverterDTO;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.model.Usuario;
import br.com.wepdev.domain.repository.PedidoRepository;
import br.com.wepdev.domain.service.EmissaoPedidoService;
import br.com.wepdev.domain.service.FluxoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/pedidos/{pedidoId}") //, produces = MediaType.APPLICATION_JSON_VALUE) // Toda a classe produz JSON
public class FluxoPedidoController {

	@Autowired
	private FluxoPedidoService fluxoPedidoService;




	@PutMapping("/confirmacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void confirmar(@PathVariable Long pedidoId){
		fluxoPedidoService.confirmar(pedidoId);
	}


	@PutMapping("/cancelamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable Long pedidoId) {
		fluxoPedidoService.cancelar(pedidoId);
	}


	@PutMapping("/entrega")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void entregar(@PathVariable Long pedidoId) {
		fluxoPedidoService.entregar(pedidoId);
	}


}
