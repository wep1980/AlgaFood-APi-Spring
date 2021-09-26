package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.GrupoNaoEncontradoException;
import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.exception.UsuarioNaoEncontradoException;
import br.com.wepdev.domain.model.Grupo;
import br.com.wepdev.domain.model.Usuario;
import br.com.wepdev.domain.repository.GrupoRepository;
import br.com.wepdev.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {


	private static final String MSG_GRUPO_EM_USO
			= "Grupo de código %d não pode ser removido, pois está em uso";


	@Autowired
	private UsuarioRepository usuarioRepository;



	@Transactional
	public Usuario salvar(Usuario usuario) {

		/**
		 * Retirando o objeto usuario do contexto de persistencia do jpa. Dessa forma é evitado o sincronismo nesse caso, de um email ser atualizado
		 * antes de a busca do email abaixo e a validação acontecer. Metodo criado em CustomJpaRepository, implementado na classe
		 * CustomJpaRepositoryImpl
		 */
		usuarioRepository.detach(usuario);

		Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

		if(usuarioExistente.isPresent()){ // Se encontrar um usuario com o mesmo email recebido no metodo, lança a exception
			throw  new NegocioException(String.format("Já existe um usuário cadastrado com o email %s", usuario.getEmail()));
		}

		return usuarioRepository.save(usuario);
	}

	@Transactional
	public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
		Usuario usuario = buscarOuFalhar(usuarioId);

		if (usuario.senhaNaoCoincideCom(senhaAtual)) {
			throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
		}

		usuario.setSenha(novaSenha);
	}

	public Usuario buscarOuFalhar(Long usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
	}
}
