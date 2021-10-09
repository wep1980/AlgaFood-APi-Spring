package br.com.wepdev.api.controller;

import br.com.wepdev.api.inputDTO.PedidoInputDTO;
import br.com.wepdev.api.DTO.PedidoDTO;
import br.com.wepdev.api.DTO.PedidoResumoDTO;
import br.com.wepdev.api.converter.*;
import br.com.wepdev.core.data.PageableTradutor;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.model.Usuario;
import br.com.wepdev.domain.repository.PedidoRepository;
import br.com.wepdev.domain.filter.PedidoFilter;
import br.com.wepdev.domain.service.EmissaoPedidoService;
import br.com.wepdev.infrastructure.repository.spec.PedidoSpecs;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
//	@GetMapping
//	public List<PedidoResumoDTO> pesquisar(PedidoFilterInputDTO filtro) {
//
//		/**
//		 * O findAll() nao estava aceitando passar um specification no parametro pq o Repository tem que extender JpaSpecificationExecutor
//		 */
//		List<Pedido> todosPedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro));
//
//		return pedidoResumoConverterDTO.converteListaEntidadeParaListaDto(todosPedidos);
//	}


	@GetMapping
	public Page<PedidoResumoDTO> pesquisarComPaginacao(PedidoFilter filtro, @PageableDefault(size = 10) Pageable pageable) {

		/**
		 * Exemplo : No PedidoResumoDO houve uma mudança na propriedade UsuarioDTO cliente para String nomeCliente, o modelMapper vai realizar o
		 * mapeamento automaticamente. So que para o consumidor da api na hora de fazer um sort(ordenar a lista por um campo) nomeCliente ocorrera um erro
		 * avisando que nao existe a propriedade nomeCliente na entidade Pedido, mas existem um cliente e dentro dele tem um nome, ou seja a propriedade
		 * cliente.nome funcionaria. Se no DTO existe unma propriedade nomeCliente, ela deve funcionar no sort, o metodo abaixo faz a traducao das propriedades
		 */
		pageable = traduzirPageable(pageable);

		Page<Pedido> pedidosPage = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), pageable);

		List<PedidoResumoDTO> pedidoResumoDto = pedidoResumoConverterDTO.converteListaEntidadeParaListaDto(pedidosPage.getContent());

		Page<PedidoResumoDTO> pedidosResumoDtoPage = new PageImpl<>(pedidoResumoDto, pageable, pedidosPage.getTotalElements());

		return pedidosResumoDtoPage;
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


	/**
	 * Metodo que traduz as propriedades que sao passadas na representacao por json. Exemplo : o consumidor da api passa nomeCliente, e o tradutor transofrma
	 * em cliente.nome
	 */
	private Pageable traduzirPageable(Pageable apiPageable){

		/**
		 * Criado um dePara de propriedades enviadas pelo consumidor da APi com as propriedades da entidade Pedido.
		 * Somente as propriedades que estiverem no mapeamento serão ordenaveis na paginação, isso e necessario quando o nome das propriedades nao coincidem, exemplo:
		 * nomeCliente -> não coincide, cliente.nome -> coincide ::: e so verificar as propriedades no DTO PedidoResumoDTO
		 */
		var mapeamento = ImmutableMap.of(
			   "codigo", "codigo", // codigo -> enviado pelo consumidor da api, codigo -> e a tradução
			  "restaurante.nome", "restaurante.nome",
			  "nomeCliente", "cliente.nome", // nomeCliente -> enviado pelo consumidor da api, cliente.nome -> e a tradução
			   "valorTotal", "valorTotal"
	   );
	   return PageableTradutor.tradutor(apiPageable, mapeamento);
	}

}
