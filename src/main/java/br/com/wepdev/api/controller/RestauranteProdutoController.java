package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTOentrada.ProdutoInputDTO;
import br.com.wepdev.api.DTO.ProdutoDTO;
import br.com.wepdev.api.converter.ProdutoConverterDTO;
import br.com.wepdev.api.converter.ProdutoInputConverterProduto;
import br.com.wepdev.domain.model.Produto;
import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.ProdutoRepository;
import br.com.wepdev.domain.service.ProdutoService;
import br.com.wepdev.domain.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima, Essa classe e a responsavel pelas respostas HTTP
@RequestMapping(value = "/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController {
	
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private RestauranteService restauranteService;

	@Autowired
	private ProdutoConverterDTO produtoConverterDTO;

	@Autowired
	private ProdutoInputConverterProduto produtoInputConverterProduto;


	/**
	 *
	 * @param restauranteId
	 * @param incluirInativos required = false -> nao torna obrigatorio a passagem do parametro, ja que por padrao a busca pelos restaurantes e feita so com o ativos
	 * @return
	 */
	@GetMapping
	public List<ProdutoDTO> listar(@PathVariable Long restauranteId, @RequestParam(required = false) boolean incluirInativos) {

		Restaurante restaurante = restauranteService.buscarOuFalhar(restauranteId);

		List<Produto> todosProdutos = null;

		if(incluirInativos){
			todosProdutos = produtoRepository.findTodosByRestaurante(restaurante);
		} else {
			todosProdutos = produtoRepository.findAtivosByRestaurante(restaurante);
		}
		return produtoConverterDTO.converteListaEntidadeParaListaDto(todosProdutos);
	}


	@GetMapping("/{produtoId}")
	public ProdutoDTO buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {

		Produto produto = produtoService.buscarOuFalhar(restauranteId, produtoId);

	    return produtoConverterDTO.converteEntidadeParaDto(produto);
	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoDTO adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInputDTO produtoInput) {

		Restaurante restaurante = restauranteService.buscarOuFalhar(restauranteId);

		Produto produto = produtoInputConverterProduto.converteInputParaEntidade(produtoInput);
		produto.setRestaurante(restaurante);

		produto = produtoService.salvar(produto);

		return produtoConverterDTO.converteEntidadeParaDto(produto);

	}


	@PutMapping("/{produtoId}")
	public ProdutoDTO atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestBody @Valid ProdutoInputDTO produtoInput) {

		Produto produtoAtual = produtoService.buscarOuFalhar(restauranteId, produtoId);

		produtoInputConverterProduto.copiaInputParaEntidade(produtoInput, produtoAtual);

		produtoAtual = produtoService.salvar(produtoAtual);

		return produtoConverterDTO.converteEntidadeParaDto(produtoAtual);
	}

}
