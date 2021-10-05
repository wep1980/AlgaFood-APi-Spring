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
import br.com.wepdev.api.DTO.INPUT.PedidoFilterInputDTO;
import br.com.wepdev.domain.service.EmissaoPedidoService;
import br.com.wepdev.infrastructure.repository.spec.PedidoSpecs;
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


	/**
	 * Metodo que pesquisa um pedido pelos filtros de restauranteId, clienteId, dataCriacaoInicio e dataCriacaoFim, ou se nao for colado nenhum filtro traz todos
	 * os pedidos.
	 *
	 * So de colocar o parametro no metodo PedidoFilterDTO, o spring ja entende e faz de forma automatica uma instancia de PedidoFilterDTO e atribui as
	 * propriedades
	 * @param filtro
	 * @return
	 */
	@GetMapping
	public List<PedidoResumoDTO> pesquisar(PedidoFilterInputDTO filtro) {

		/**
		 * O findAll() nao estava aceitando passar um specification no parametro pq o Repository tem que extender JpaSpecificationExecutor
		 */
		List<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro));

		return pedidoResumoConverterDTO.converteListaEntidadeParaListaDto(todosPedidos);
	}


	/**
	 * Endpoint para trabalhar com JsonFilter, recebe por parametro somente os campos desejados que seja repassados na representação,
	 * ou repassa na representacao todos os campos, caso nenhum parametro seja passado
	 *
	 * @return
	 */
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
//
//		List<Pedido> pedidos = pedidoRepository.findAll();
//		List<PedidoResumoDTO> pedidosResumoDto = pedidoResumoConverterDTO.converteListaEntidadeParaListaDto(pedidos);
//
//		MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosResumoDto); // Instanciando o envelope passado o DTO
//
//		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
//		//filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll()); // Serializa todas as propriedades do DTO
//
//		// Serializa apenas as propriedades codigo e valorTotal do DTO
//		//filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept("codigo", "valorTotal"));
//
//		filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll()); // por padrao serializa todas as propriedades do DTO
//
//		if(StringUtils.isNotBlank(campos)){
//           filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept( // se tiver campos serializa pelos campos passados
//				   campos.split(","))); // Quebra a String campos , separando por virgula em um array
//		}
//		pedidosWrapper.setFilters(filterProvider);
//
//		return pedidosWrapper;
//	}



	@GetMapping("/{codigoPedido}")
	public PedidoDTO buscar(@PathVariable String codigoPedido) {

		Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);

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
