package br.com.wepdev.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;


/**
 * Classe para implementacao de regras de negocio
 * @author Waldir
 *
 */
@Service
public class CozinhaService {
	
	
	@Autowired
	private CozinhaRepository repository;
	
	
	
	public Cozinha salvarOuAtualiza(Cozinha cozinha) {
		return repository.salvarOuAtualizar(cozinha);
	}
	
	
	public void remover(Long cozinhaId) {
		try {
			repository.remover(cozinhaId);
			
		} catch (EmptyResultDataAccessException e) {//Classe customizada(Excessao de negocio) de excecao para traduzir a excessao acima, EmptyResultDataAccessException
			throw new EntidadeNaoEncontradaException(String.format("Não existe um cadastro de cozinha com código %d", cozinhaId));//String.format -> formatando a mensagem
			
		} catch (DataIntegrityViolationException e) { //Classe customizada(Excessao de negocio) de excecao para traduzir a excessao acima, DataIntegrityViolationException
			throw new EntidadeEmUsoException(String.format("Cozinha de código %d não pode ser removida, pois está em uso", cozinhaId));//String.format -> formatando a mensagem
		}
	}

}
