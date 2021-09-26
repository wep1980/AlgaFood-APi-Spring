package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.GrupoDTO;
import br.com.wepdev.api.DTO.INPUT.GrupoInputDTO;
import br.com.wepdev.api.converter.GrupoConverterDTO;
import br.com.wepdev.api.converter.GrupoInputConverterGrupo;
import br.com.wepdev.domain.model.Grupo;
import br.com.wepdev.domain.repository.GrupoRepository;
import br.com.wepdev.domain.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima, Essa classe e a responsavel pelas respostas HTTP
@RequestMapping(value = "/grupos")
public class GrupoController {
	
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private GrupoService grupoService;

	@Autowired
	private GrupoConverterDTO grupoConverterDTO;

	@Autowired
	private GrupoInputConverterGrupo grupoInputConverterGrupo;



	@GetMapping
	public List<GrupoDTO> listar() {
		List<Grupo> todosGrupos = grupoRepository.findAll();

		return grupoConverterDTO.converteListaEntidadeParaListaDto(todosGrupos);
	}

	@GetMapping("/{grupoId}")
	public GrupoDTO buscar(@PathVariable Long grupoId) {
		Grupo grupo = grupoService.buscarOuFalhar(grupoId);

		return grupoConverterDTO.converteEntidadeParaDto(grupo);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GrupoDTO adicionar(@RequestBody @Valid GrupoInputDTO grupoInput) {
		Grupo grupo = grupoInputConverterGrupo.converteInputParaEntidade(grupoInput);

		grupo = grupoService.salvar(grupo);

		return grupoConverterDTO.converteEntidadeParaDto(grupo);
	}

	@PutMapping("/{grupoId}")
	public GrupoDTO atualizar(@PathVariable Long grupoId,
								@RequestBody @Valid GrupoInputDTO grupoInput) {
		Grupo grupoAtual = grupoService.buscarOuFalhar(grupoId);

		grupoInputConverterGrupo.copiaInputParaEntidade(grupoInput, grupoAtual);

		grupoAtual = grupoService.salvar(grupoAtual);

		return grupoConverterDTO.converteEntidadeParaDto(grupoAtual);
	}

	@DeleteMapping("/{grupoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long grupoId) {
		grupoService.excluir(grupoId);
	}


}
