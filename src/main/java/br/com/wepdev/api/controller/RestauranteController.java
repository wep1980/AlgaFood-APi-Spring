package br.com.wepdev.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.com.wepdev.Grupos;
import br.com.wepdev.domain.exception.CozinhaNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.exception.RestauranteNaoEncontradoException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.RestauranteRepository;
import br.com.wepdev.domain.service.RestauranteService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired 
	private RestauranteRepository restauranteRepository;

	
	
	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}
	
	
	@GetMapping("/{restauranteId}")
	public Restaurante buscar(@PathVariable Long restauranteId) {
		return restauranteService.buscarOuFalhar(restauranteId);

	}


	/**
	 * @Valid -> a validação da instancia de Restaurante e feita antes de o metodo ser chamado, ele sempre valida as propriedades que possuem
	 * as anotações para validações e essas anotações por padrão fazem parte de um grupo default, exemplo : @NotBlank(groups = Default.class)
	 * @Validated(Grupos.CadastroRestaurante.class) -> Valida Restaurante pelo grupo CadastroRestaurante.class, que é um grupo criado para validar somente algumas
	 * propriedades que fazem parte desse grupo (Grupos.CadastroRestaurante.class).
	 * @param restaurante
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurante adicionar(@RequestBody @Validated(Grupos.CadastroRestaurante.class) Restaurante restaurante) {
		try {
			return restauranteService.salvar(restaurante);
		}catch (CozinhaNaoEncontradaException e) { // Exception caso a cozinha nao exista na hora de adicionar um restaurante
			throw new NegocioException(e.getMessage());
		}
	}


	@PutMapping("/{restauranteId}")
	public Restaurante atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaurante) {
		try {
			//Busca o restaurante atual ou lança uma exception que esta com NOT.FOUND
			Restaurante restauranteAtual = restauranteService.buscarOuFalhar(restauranteId);
			// Copia a instancia de restaurante para restauranteAtual, exceto o id, formasPagamento, endereco, dataCadastro
			BeanUtils.copyProperties(restaurante, restauranteAtual, "id" , "formasPagamento", "endereco", "dataCadastro", "produtos");
			// Salva e retorna o corpo, e a resposta HTTP e enviada como 200 -> OK
			return restauranteService.salvar(restauranteAtual);
		} catch (CozinhaNaoEncontradaException e){ // Execessao lançada caso na hora de atualizar um restaurante a cozinha não exista
			throw new NegocioException(e.getMessage(), e); // e -> Mostra a causa da exception na representacao(POSTMAN)
		}

	}


	/**
	 * HttpServletRequest request -> O Spring ja passa para o metodo automaticamente implicitamente.
	 * @param restauranteId
	 * @param campos
	 * @param request
	 * @return
	 */
	@PatchMapping("/{restauranteId}")
	public Restaurante atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos, HttpServletRequest request) {
		//Busca o restaurante atual ou lança uma exception que esta com NOT.FOUND
		Restaurante restauranteAtual = restauranteService.buscarOuFalhar(restauranteId);

		// Atribui os valores do campos para dentro do restauranteAtual
		merge(campos, restauranteAtual, request);
		
		return atualizar(restauranteId, restauranteAtual);
	}

	
	   /**
	     * Metodo que mescla os valores do postman com os valores armazendos no Banco de dados
		 * Expressao Lambda
		 * nomePropriedade -> String do Map
		 * valorPropriedade -> Object do Map
		 * REFLECTIONS -> INSPECIONA OBJETOS JAVA EM TEMPO DE EXECUÇÃO DE FORMA DINAMICA
		*
		* HttpServletRequest request -> O Spring ja passa para o metodo automaticamente implicitamente.
		 */
	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
		// Foi necessario instancia-lo para passar de argumento na resposta, e assim na hora de relançar a exception IllegalArgumentException para
		// HttpMessageNotReadableException que esta depreciada, sera lançada no lugar o mesmo, so que esse possui no construtor o argumento do ServletServerHttpRequest
		//que nao esta depreciado.
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		try{
			// Serializa e converte objetos java em JSON e ao contrario tb, realiza configurações nos objetos que estao dentro desse metodo.
			ObjectMapper objectMapper = new ObjectMapper();
			// Falha, lança exception caso a propriedade esteja ignorada na entidade e seja passada na representação
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			// Falha, lança exception caso a propriedade nao exista na entidade e seja passada na representação
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			// Criando um objeto Restaurante com os dados da origem(POSTMAN), JA CONVERTIDO E EVITANDO ERROS
			Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

			// Pegando as propriedades passadas pelo cliente
			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				// field -> representa um atributo da classe Restaurante
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);  // Retorna a instancia de um campo
				field.setAccessible(true);// Acessa variavel private

				// Buscando o valor da propriedade representada pelo field, dentro da instancia de restauranteOrigem
				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

//			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);

				// field -> atribuindo o valorPropriedade de instancia que é o nome, no nome da do restauranteDestino(Restaurante "nome")
				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		}catch (IllegalArgumentException e){ // Captura a exception IllegalArgumentException e relança a exception como HttpMessageNotReadableException
             Throwable rootCause = ExceptionUtils.getRootCause(e);
			// Captura a exception IllegalArgumentException e relança a exception como HttpMessageNotReadableException
			 throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}


	}

}
