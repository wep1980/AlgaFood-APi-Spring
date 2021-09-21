package br.com.wepdev.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.wepdev.api.DTO.CozinhaDTO;
import br.com.wepdev.api.DTO.INPUT.RestauranteINPUT;
import br.com.wepdev.api.DTO.RestauranteDTO;
import br.com.wepdev.core.validation.ValidacaoException;
import br.com.wepdev.domain.exception.CozinhaNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.model.Cozinha;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.RestauranteRepository;
import br.com.wepdev.domain.service.RestauranteService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired 
	private RestauranteRepository restauranteRepository;


	/*
	SmartValidator -> Api do beanValidation, recebe uma instancia para validação
	 */
	@Autowired
	private SmartValidator validato;

	
	
	@GetMapping
	public List<RestauranteDTO> listar() {
		return toCollectionModel(restauranteRepository.findAll());
	}


	/**
	 * Metodo que converte uma lista de Restaurante em uma lista de RestauranteDTO
	 * @param restaurantes
	 * @return
	 */
	private List<RestauranteDTO> toCollectionModel(List<Restaurante> restaurantes){

		// Convertendo uma lista de Restaurantes para uma lista de RestauranteDTO
		return restaurantes.stream().map(restaurante -> toModel(restaurante))
				.collect(Collectors.toList());

	}


	
	
	@GetMapping("/{restauranteId}")
	public RestauranteDTO buscar(@PathVariable Long restauranteId) {
		Restaurante  restaurante = restauranteService.buscarOuFalhar(restauranteId);

		return toModel(restaurante);
	}

	/**
	 * Metodo que converte Restaurante em RestauranteDTO
	 * @param restaurante
	 * @return
	 */
	private RestauranteDTO toModel(Restaurante restaurante) {
		CozinhaDTO cozinhaDTO = new CozinhaDTO();
		cozinhaDTO.setId(restaurante.getCozinha().getId());
		cozinhaDTO.setNome(restaurante.getCozinha().getNome());

		RestauranteDTO restauranteDTO = new RestauranteDTO();
		restauranteDTO.setId(restaurante.getId());
		restauranteDTO.setNome(restaurante.getNome());
		restauranteDTO.setTaxaFrete(restaurante.getTaxaFrete());
		restauranteDTO.setCozinha(cozinhaDTO);
		return restauranteDTO;
	}


	/**
	 * @Valid -> a validação da instancia de Restaurante e feita antes de o metodo ser chamado, ele sempre valida as propriedades que possuem
	 * as anotações para validações e essas anotações por padrão fazem parte de um grupo default, exemplo : @NotBlank(groups = Default.class)
	 * @Validated(Grupos.CadastroRestaurante.class) -> Valida Restaurante todas as propriedades que possuem o groups CadastroRestaurante.class,
	 * é um grupo criado para validar somente as propriedades que fazem parte desse grupo (Grupos.CadastroRestaurante.class).
	 * @param restaurante
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteDTO adicionar(@RequestBody @Valid RestauranteINPUT restauranteInput) {

		try {
			Restaurante restaurante = toDomainObject(restauranteInput); // Transformando um restauranteInput para um Restaurante

			return toModel(restauranteService.salvar(restaurante));
		}catch (CozinhaNaoEncontradaException e) { // Exception caso a cozinha nao exista na hora de adicionar um restaurante
			throw new NegocioException(e.getMessage());
		}
	}


	/**
	 * Metodo que transforma RestauranteInput para Restaurante
	 * @param restauranteInput
	 * @return
	 */
	private Restaurante toDomainObject(RestauranteINPUT restauranteInput){
		Restaurante restaurante = new Restaurante();
		restaurante.setNome(restauranteInput.getNome());
		restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());

		Cozinha cozinha = new Cozinha();
		cozinha.setId(restauranteInput.getCozinha().getId());

		restaurante.setCozinha(cozinha);

		return restaurante;
	}


	@PutMapping("/{restauranteId}")
	public RestauranteDTO atualizar(@PathVariable Long restauranteId, @RequestBody @Valid RestauranteINPUT restauranteInput) {
		try {
			Restaurante restaurante = toDomainObject(restauranteInput); // Transformando um restauranteInput para um Restaurante
			//Busca o restaurante atual ou lança uma exception que esta com NOT.FOUND
			Restaurante restauranteAtual = restauranteService.buscarOuFalhar(restauranteId);
			// Copia a instancia de restaurante para restauranteAtual, exceto o id, formasPagamento, endereco, dataCadastro
			BeanUtils.copyProperties(restaurante, restauranteAtual, "id" , "formasPagamento", "endereco", "dataCadastro", "produtos");
			// Salva e retorna o corpo, e a resposta HTTP e enviada como 200 -> OK
			return toModel(restauranteService.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException e){ // Execessao lançada caso na hora de atualizar um restaurante a cozinha não exista
			throw new NegocioException(e.getMessage(), e); // e -> Mostra a causa da exception na representacao(POSTMAN)
		}

	}


//	/**
//	 * Metodo que atualiza apenas propriedades selecionadas, nao o objeto inteiro.
//	 * HttpServletRequest request -> O Spring ja passa para o metodo automaticamente implicitamente.
//	 *
//	 * Esse endpoint nao recebe uma classe, e sim um map<String, Object>, dessa forma o spring nao consegue fazer um binding e chamar o bean validation para fazer a validação
//	 * do objeto
//	 *
//	 * @param restauranteId
//	 * @param campos
//	 * @param request
//	 * @return
//	 */
//	@PatchMapping("/{restauranteId}")
//	public Restaurante atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos, HttpServletRequest request) {
//
//		//Busca o restaurante atual ou lança uma exception que esta com NOT.FOUND
//		Restaurante restauranteAtual = restauranteService.buscarOuFalhar(restauranteId);
//
//		// Atribui os valores do campos para dentro do restauranteAtual
//		merge(campos, restauranteAtual, request);
//
//		/*
//		metodo para validação dos campos que estao sendo atualizados. Recebe como parametro o objeto que sera validado(restauranteAtual) e o nome
//		do objeto(restaurante)
//		 */
//		validate(restauranteAtual, "restaurante");
//
//		return atualizar(restauranteId, restauranteAtual);
//	}
//
//
//	private void validate(Restaurante restauranteAtual, String objectName) {
//
//		/*
//         BeanPropertyBindingResult -> classe que implementa BindingResult que extende errors.
//         Com essa instancia e possivel passar os erros caso ocorra.
//         Recebe como parametro o objeto que sera validado(restauranteAtual) e o objectName(nome do objeto)
//		 */
//		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restauranteAtual, objectName);
//
//		/*
//		restauranteAtual -> objeto que vai ser validado.
//		No segundo parametro e necessario informar errors, o bindingResult extende errors, e é ele que sera passado no segundo
//		parametro
//
//		 */
//		validato.validate(restauranteAtual, bindingResult);
//
//		if(bindingResult.hasErrors()){ // Verifica se tem erros dentro do bindingResult
//          throw new ValidacaoException(bindingResult); // Lança uma exception customizada
//		}
//	}
//
//
//	/**
//	     * Metodo que mescla os valores do postman com os valores armazendos no Banco de dados
//		 * Expressao Lambda
//		 * nomePropriedade -> String do Map
//		 * valorPropriedade -> Object do Map
//		 * REFLECTIONS -> INSPECIONA OBJETOS JAVA EM TEMPO DE EXECUÇÃO DE FORMA DINAMICA
//		*
//		* HttpServletRequest request -> O Spring ja passa para o metodo automaticamente implicitamente.
//		 */
//	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
//		// Foi necessario instancia-lo para passar de argumento na resposta, e assim na hora de relançar a exception IllegalArgumentException para
//		// HttpMessageNotReadableException que esta depreciada, sera lançada no lugar o mesmo, so que esse possui no construtor o argumento do ServletServerHttpRequest
//		//que nao esta depreciado.
//		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
//		try{
//			// Serializa e converte objetos java em JSON e ao contrario tb, realiza configurações nos objetos que estao dentro desse metodo.
//			ObjectMapper objectMapper = new ObjectMapper();
//			// Falha, lança exception caso a propriedade esteja ignorada na entidade e seja passada na representação
//			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
//			// Falha, lança exception caso a propriedade nao exista na entidade e seja passada na representação
//			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
//			// Criando um objeto Restaurante com os dados da origem(POSTMAN), JA CONVERTIDO E EVITANDO ERROS
//			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);
//
//			// Pegando as propriedades passadas pelo cliente
//			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
//				// field -> representa um atributo da classe Restaurante
//				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);  // Retorna a instancia de um campo
//				field.setAccessible(true);// Acessa variavel private
//
//				// Buscando o valor da propriedade representada pelo field, dentro da instancia de restauranteOrigem
//				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
//
////			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
//
//				// field -> atribuindo o valorPropriedade de instancia que é o nome, no nome da do restauranteDestino(Restaurante "nome")
//				ReflectionUtils.setField(field, restauranteDestino, novoValor);
//			});
//		}catch (IllegalArgumentException e){ // Captura a exception IllegalArgumentException e relança a exception como HttpMessageNotReadableException
//             Throwable rootCause = ExceptionUtils.getRootCause(e);
//			// Captura a exception IllegalArgumentException e relança a exception como HttpMessageNotReadableException
//			 throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
//		}
//	}

}
