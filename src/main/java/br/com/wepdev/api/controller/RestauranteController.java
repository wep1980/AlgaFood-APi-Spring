package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.RestauranteDTO;
import br.com.wepdev.api.converter.RestauranteConverterDTO;
import br.com.wepdev.api.converter.RestauranteInputConverterRestaurante;
import br.com.wepdev.api.inputDTO.RestauranteInputDTO;
import br.com.wepdev.domain.exception.CidadeNaoEncontradaException;
import br.com.wepdev.domain.exception.CozinhaNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.exception.RestauranteNaoEncontradoException;
import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.model.view.RestauranteView;
import br.com.wepdev.domain.repository.RestauranteRepository;
import br.com.wepdev.domain.service.RestauranteService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//@CrossOrigin(origins = {"http://www.algafood.local:8000", ""}) // Liberando CORS no controller completo, e para liberar mais de 1 URL e so colocar um array como no exemplo

/*
// tempo maximo de 20 segundos que o browser pode armazenar o tempo do preflight do CORS. preflight é uma primeira requisição feita pelo navegador antes da requisição
 de fato feita por nos. O preflight so feito quando a requisição e feita para uma origem diferente, e apenas em certas condições, se for uma requisição simples dos verbos
 (GET, POST, HEAD) nao vai ativar o preflight, ou seja, nao vai fazer o options antes. Cabeçalhos tambem definem se uma requisição e simples ou não
 informações em https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS#simple_requests
 */
//@CrossOrigin(maxAge = 20)
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



	//	@JsonView(RestauranteView.Resumo.class) // Ao inves de representação ser exibido o DTO, sera exibido a classe de resumo que utiliza o JsonView
//	@GetMapping(params = "projecao=resumo") // esse endpoint so sera utilizado se no GET for passado esse parametro=resumo
//	public List<RestauranteDTO> listarResumido() {
//		return listar();
//	}


	//@CrossOrigin -> Pode ser utilizado no metodo, dessa forma e como se fosse : @CrossOrigin(origins = "*"), liberado para todas as URLs
	@ApiOperation(value = "Lista restaurantes")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "Nome da projecão de pedidos", allowableValues = "apenas-nome" , name = "projecao", paramType = "query", dataTypeClass = String.class)
	})
	@JsonView(RestauranteView.Resumo.class)
	@GetMapping
	public List<RestauranteDTO> listar() {
		return restauranteConverterDTO.converteListaEntidadeParaListaDto(restauranteRepository.findAll());
	}


	//hidden = true -> Ocultando essa operacao da documentacao, a que sera exibida e a operacao acima, a de listar(), nela informamaos que pode ser passado um parametro opicinal, o apenas-nome
	@ApiOperation(value = "Lista restaurantes", hidden = true)
	@JsonView(RestauranteView.ApenasNome.class) // Ao inves de representação ser exibido o DTO, sera exibido a classe de resumo que utiliza o JsonView
	@GetMapping(params = "projecao=apenas-nome") // esse endpoint so sera utilizado se no GET for passado esse parametro=apenas-nome
	public List<RestauranteDTO> listarApenasNomes() {
		return listar();
	}

	/**
	 * Deixando o endpoint dinamico para aceitar parametros diferentes utilizando MappingJacksonValue.
	 * O metodo recebe um @RequestParam e o required = false, para não ser obrigatorio o parametro, ja que ao listar sem passagem de parametro,
	 * na representação ele traz a lista de acordo com o DTO e nao as interfaces com JsonView que possuem os parametros
	 * @return
	 */
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
//		List<Restaurante> restaurantes = restauranteRepository.findAll();
//		List<RestauranteDTO> restaurantesDto = restauranteConverterDTO.converteListaEntidadeParaListaDto(restaurantes);
//
//		MappingJacksonValue restaurantesWrapper = new MappingJacksonValue(restaurantesDto);
//
//		// se nao for passado nenhum parametro, por padrão vai ser sempre utilizado o resumo na representação
//		restaurantesWrapper.setSerializationView(RestauranteView.Resumo.class);
//
//		if("apenas-nome".equals(projecao)){ // Se o parametro passado for projecao
//			restaurantesWrapper.setSerializationView(RestauranteView.ApenasNome.class);
//		}
//		else if("completo".equals(projecao)){ // se o parametro passado for completo, o DTO sera passado na representação, por isso valor null abaixo
//			restaurantesWrapper.setSerializationView(null);
//		}
//		return restaurantesWrapper;
//	}

	
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

    @PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarVarios(@RequestBody List<Long> restaurantesIds){
		try {
			restauranteService.ativarVarios(restaurantesIds);
		} catch (RestauranteNaoEncontradoException e){
			throw new NegocioException(e.getMessage(), e);
		}


	}

	@DeleteMapping("/desativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarVarios(@RequestBody List<Long> restaurantesIds){
		try {
			restauranteService.inativarVarios(restaurantesIds);

		} catch (RestauranteNaoEncontradoException e){
			throw new NegocioException(e.getMessage(), e);
		}

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
