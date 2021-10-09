package br.com.wepdev.api.controller;

import java.util.List;

import br.com.wepdev.api.DTO.EstadoDTO;
import br.com.wepdev.api.inputDTO.EstadoInputDTO;
import br.com.wepdev.api.converter.EstadoConverterDTO;
import br.com.wepdev.api.converter.EstadoInputConverterEstado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.wepdev.domain.model.Estado;
import br.com.wepdev.domain.repository.EstadoRepository;
import br.com.wepdev.domain.service.EstadoService;

import javax.validation.Valid;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima
@RequestMapping(value = "/estados")
public class EstadoController {
	
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private EstadoService estadoService;

	@Autowired
	private EstadoConverterDTO estadoConverterDTO;

	@Autowired
	private EstadoInputConverterEstado estadoInputConverterEstado;
	


	@GetMapping
	public List<EstadoDTO> listar() {

		List<Estado> todosEstados = estadoRepository.findAll();

		return estadoConverterDTO.converteListaEntidadeParaListaDto(todosEstados);
	}


	@GetMapping("/{estadoId}")
	public EstadoDTO buscar(@PathVariable Long estadoId) {

		Estado estado = estadoService.buscarOuFalhar(estadoId);

		return estadoConverterDTO.converteEntidadeParaDto(estado);
	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoDTO adicionar(@RequestBody @Valid EstadoInputDTO estadoInputDTO) {
		Estado estado = estadoInputConverterEstado.converteInputParaEntidade(estadoInputDTO);

		estado = estadoService.salvar(estado);
		return estadoConverterDTO.converteEntidadeParaDto(estado);
	}


	@PutMapping("/{estadoId}")
	public EstadoDTO atualizar(@PathVariable Long estadoId, @RequestBody @Valid EstadoInputDTO estadoInputDTO) {

		    Estado estadoAtual = estadoService.buscarOuFalhar(estadoId);

			estadoInputConverterEstado.copiaInputParaEntidade(estadoInputDTO, estadoAtual);

			estadoAtual = estadoService.salvar(estadoAtual);

			return estadoConverterDTO.converteEntidadeParaDto(estadoAtual);
	}


	@DeleteMapping("/{estadoId}")
	public void remover(@PathVariable Long estadoId) {

		estadoService.excluir(estadoId);
	}
	
	
	
}
