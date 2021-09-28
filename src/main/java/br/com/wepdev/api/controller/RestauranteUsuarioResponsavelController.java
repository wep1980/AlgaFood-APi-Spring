package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.INPUT.ProdutoInputDTO;
import br.com.wepdev.api.DTO.ProdutoDTO;
import br.com.wepdev.api.DTO.UsuarioDTO;
import br.com.wepdev.api.converter.ProdutoConverterDTO;
import br.com.wepdev.api.converter.ProdutoInputConverterProduto;
import br.com.wepdev.api.converter.UsuarioConverterDTO;
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
@RequestMapping(value = "/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioResponsavelController {
	


	@Autowired
	private RestauranteService restauranteService;

	@Autowired
    private UsuarioConverterDTO usuarioConverterDTO;


	@GetMapping
	public List<UsuarioDTO> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante = restauranteService.buscarOuFalhar(restauranteId);

		return usuarioConverterDTO.converteListaEntidadeParaListaDto(restaurante.getResponsaveis());
	}


	@DeleteMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteService.desassociarResponsavel(restauranteId, usuarioId);
	}

	@PutMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associar(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteService.associarResponsavel(restauranteId, usuarioId);
	}



}
