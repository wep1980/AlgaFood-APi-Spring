package br.com.wepdev.api.controller;

import br.com.wepdev.domain.service.FluxoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/pedidos/{codigoPedido}") //, produces = MediaType.APPLICATION_JSON_VALUE) // Toda a classe produz JSON
public class FluxoPedidoController {

	@Autowired
	private FluxoPedidoService fluxoPedidoService;




	@PutMapping("/confirmacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void confirmar(@PathVariable String codigoPedido){
		fluxoPedidoService.confirmar(codigoPedido);
	}


	@PutMapping("/cancelamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable String codigoPedido) {
		fluxoPedidoService.cancelar(codigoPedido);
	}


	@PutMapping("/entrega")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void entregar(@PathVariable String codigoPedido) {
		fluxoPedidoService.entregar(codigoPedido);
	}


}
