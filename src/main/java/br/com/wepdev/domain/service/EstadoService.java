package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.EstadoNaoEncontradoException;
import br.com.wepdev.domain.model.Cozinha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.model.Estado;
import br.com.wepdev.domain.repository.EstadoRepository;


/**
 * Classe para implementacao de regras de negocio
 * @author Waldir
 *
 */
@Service
public class EstadoService {


	//public static final String MSG_ESTADO_CADASTRO_NAO_ENCONTRADO = "Não existe um cadastro de estado com código %d";
	public static final String MSG_ERRO_ESTADO_USO = "Estado de código %d não pode ser removido, pois está em uso";


	@Autowired
	private EstadoRepository estadoRepository;

     
	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}
	
	public void excluir(Long estadoId) {
		try {
			estadoRepository.deleteById(estadoId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EstadoNaoEncontradoException(estadoId);
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format(MSG_ERRO_ESTADO_USO, estadoId));
		}
	}

	/**
	 * Optional -> E o tipo de objeto que o findById retorna, que pode ser qualquer entidade.
	 * orElseThrow -> Retorna o objeto que esta dentro do Optional, se nao tiver nada dentro do Optional,
	 * ele lança a excessao
	 */
	public Estado buscarOuFalhar(Long estadoId){
		return estadoRepository.findById(estadoId).orElseThrow(() -> new EstadoNaoEncontradoException(estadoId));
	}

}
