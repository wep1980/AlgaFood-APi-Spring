package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.CozinhaNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe para implementacao de regras de negocio
 * @author Waldir
 *
 */
@Service
public class CozinhaService {

	//public static final String MSG_COZINHA_NAO_ENCONTRADA = "Não existe um cadastro de cozinha com código %d";
	public static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";


	@Autowired
	private CozinhaRepository cozinhaRepository;



	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}


	/**
	 * A Transação por padrao ele termina quando o metodo do jpa deleteById e terminado, e como colocamos um @Transactional nesse metodo
	 * inteiro a exception que colocamos para tratamento de erro nao esta sendo utilizada, para resolver isso se utiliza um flush(), que
	 * compila todas as linhas do metodo antes de commitar no banco de dados
	 * @param cozinhaId
	 */
	@Transactional
	public void excluir(Long cozinhaId) {
		try {
			cozinhaRepository.deleteById(cozinhaId);
			cozinhaRepository.flush(); // Executa todas as linhas de codigo do metodo, antes de commitar no banco de dados
			
		} catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(cozinhaId);
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format(MSG_COZINHA_EM_USO, cozinhaId));
		}
	}

	/**
	 * Optional -> E o tipo de objeto que o findById retorna, que pode ser qualquer entidade.
	 * orElseThrow -> Retorna o objeto que esta dentro do Optional, se nao tiver nada dentro do Optional,
	 * ele lança a excessao
	 */
	public Cozinha buscarOuFalhar(Long cozinhaId){
		return cozinhaRepository.findById(cozinhaId).orElseThrow(() -> new CozinhaNaoEncontradaException(cozinhaId));
	}

}
