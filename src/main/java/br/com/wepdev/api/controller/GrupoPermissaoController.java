package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.api.DTO.PermissaoDTO;
import br.com.wepdev.api.converter.PermissaoConverterDTO;
import br.com.wepdev.domain.model.Grupo;
import br.com.wepdev.domain.model.Permissao;
import br.com.wepdev.domain.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima, Essa classe e a responsavel pelas respostas HTTP
@RequestMapping(value = "/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController {

	
	@Autowired
	private GrupoService grupoService;

	@Autowired
	private PermissaoConverterDTO permissaoConverterDTO;



	@GetMapping
	public List<PermissaoDTO> listar(@PathVariable Long grupoId) {
		Grupo grupo = grupoService.buscarOuFalhar(grupoId);

		return permissaoConverterDTO.converteListaEntidadeParaListaDto(grupo.getPermissoes());
	}


	@DeleteMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		grupoService.desassociarPermissao(grupoId, permissaoId);
	}



	@PutMapping("/{permissaoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
		grupoService.associarPermissao(grupoId, permissaoId);
	}



}
