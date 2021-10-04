package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.INPUT.PedidoInputDTO;
import br.com.wepdev.api.DTO.PedidoDTO;
import br.com.wepdev.api.DTO.PedidoResumoDTO;
import br.com.wepdev.api.converter.*;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.model.Usuario;
import br.com.wepdev.domain.repository.PedidoRepository;
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

	@Autowired
	private PedidoInputConverterPedido pedidoInputConverterPedido;
	


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


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoDTO adicionar(@Valid @RequestBody PedidoInputDTO pedidoInput) {
		try {
			Pedido novoPedido = pedidoInputConverterPedido.converteInputParaEntidade(pedidoInput);

			// TODO pegar usuário autenticado
			novoPedido.setCliente(new Usuario());
			novoPedido.getCliente().setId(1L);

			novoPedido = emissaoPedido.emitir(novoPedido);

			return pedidoConverterDTO.converteEntidadeParaDto(novoPedido);
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

}
