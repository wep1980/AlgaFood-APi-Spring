package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTOentrada.SenhaInputDTO;
import br.com.wepdev.api.DTOentrada.UsuarioComSenhaInputDTO;
import br.com.wepdev.api.DTOentrada.UsuarioInputDTO;
import br.com.wepdev.api.DTO.UsuarioDTO;
import br.com.wepdev.api.converter.UsuarioConverterDTO;
import br.com.wepdev.api.converter.UsuarioInputConverterUsuario;
import br.com.wepdev.domain.model.Usuario;
import br.com.wepdev.domain.repository.UsuarioRepository;
import br.com.wepdev.domain.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


//@ResponseBody // As Respostas dos metedos desse controlador devem ir na resposta da requisicao
//@Controller // Controlador REST
@RestController // Substitue as 2 anotacoes acima, Essa classe e a responsavel pelas respostas HTTP
@RequestMapping(value = "/usuarios")
public class UsuarioController {
	
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioConverterDTO usuarioConverterDTO;

	@Autowired
	private UsuarioInputConverterUsuario usuarioInputConverterUsuario;

	
	
	@GetMapping
	public List<UsuarioDTO> listar() {

		List<Usuario> todosUsuarios = usuarioRepository.findAll();

		return usuarioConverterDTO.converteListaEntidadeParaListaDto(todosUsuarios);
	}


	@GetMapping("/{usuarioId}")
	public UsuarioDTO buscar(@PathVariable Long usuarioId) {

		Usuario usuario = usuarioService.buscarOuFalhar(usuarioId);

	    return usuarioConverterDTO.converteEntidadeParaDto(usuario);
	}


	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioDTO adicionar(@RequestBody @Valid UsuarioComSenhaInputDTO usuarioInput) {

			Usuario usuario = usuarioInputConverterUsuario.converteInputParaEntidade(usuarioInput);

			usuario = usuarioService.salvar(usuario);

			return usuarioConverterDTO.converteEntidadeParaDto(usuario);
	}


	@PutMapping("/{usuarioId}")
	public UsuarioDTO atualizar(@PathVariable Long usuarioId, @RequestBody @Valid UsuarioInputDTO usuarioInput) {

				Usuario usuarioAtual = usuarioService.buscarOuFalhar(usuarioId);

				usuarioInputConverterUsuario.copiaInputParaEntidade(usuarioInput, usuarioAtual);

				usuarioAtual = usuarioService.salvar(usuarioAtual);

				return usuarioConverterDTO.converteEntidadeParaDto(usuarioAtual);
	}


	@PutMapping("/{usuarioId}/senha")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid SenhaInputDTO senha) {

		usuarioService.alterarSenha(usuarioId, senha.getSenhaAtual(), senha.getNovaSenha());
	}


}
