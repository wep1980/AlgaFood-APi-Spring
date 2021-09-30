package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.CozinhaDTO;
import br.com.wepdev.api.DTO.INPUT.CozinhaInputDTO;
import br.com.wepdev.api.DTO.PedidoDTO;
import br.com.wepdev.api.DTO.PedidoResumoDTO;
import br.com.wepdev.api.converter.CozinhaConverterDTO;
import br.com.wepdev.api.converter.CozinhaInputConverterCozinha;
import br.com.wepdev.api.converter.PedidoConverterDTO;
import br.com.wepdev.api.converter.PedidoResumoConverterDTO;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.repository.CozinhaRepository;
import br.com.wepdev.domain.repository.PedidoRepository;
import br.com.wepdev.domain.service.CozinhaService;
import br.com.wepdev.domain.service.EmissaoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/pedidos") //, produces = MediaType.APPLICATION_JSON_VALUE) // Toda a classe produz JSON
public class PedidoController {
	
	
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private EmissaoPedidoService emissaoPedido;

	@Autowired
	private PedidoConverterDTO pedidoConverterDTO;

	@Autowired
	private PedidoResumoConverterDTO pedidoResumoConverterDTO;
	


	@GetMapping
	public List<PedidoResumoDTO> listar() {

		List<Pedido> todosPedidos = pedidoRepository.findAll();

		return pedidoResumoConverterDTO.converteListaEntidadeParaListaDto(todosPedidos);
	}



	@GetMapping("/{pedidoId}")
	public PedidoDTO buscar(@PathVariable Long pedidoId) {

		Pedido pedido = emissaoPedido.buscarOuFalhar(pedidoId);

		return pedidoConverterDTO.converteEntidadeParaDto(pedido);
	}


}
