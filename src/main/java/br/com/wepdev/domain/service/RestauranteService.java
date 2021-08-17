package br.com.wepdev.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.CozinhaRepository;
import br.com.wepdev.domain.repository.RestauranteRepository;


/**
 * Classe para implementacao de regras de negocio
 * @author Waldir
 *
 */
@Service
public class RestauranteService {
	 
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaRepository.findById(cozinhaId)
				// orElseThrow() -> Retorna a instancia de cozinha que esta dentro do Optional, se nao tiver nada dentro, lança a exception com lambda
				.orElseThrow(() -> new EntidadeNaoEncontradaException(String.format("Não existe cadastro de cozinha com código %d", cozinhaId)));
	
		restaurante.setCozinha(cozinha);
		return restauranteRepository.salvarOuAtualizar(restaurante);
	}
	
	
//    public void remover(Long restauranteId) {
//        try {
//        	restauranteRepository.remover(restauranteId);
//            
//        } catch (EmptyResultDataAccessException e) {
//            throw new EntidadeNaoEncontradaException(
//                String.format("Não existe um cadastro de cidade com código %d", restauranteId));
//        
//        } catch (DataIntegrityViolationException e) {
//            throw new EntidadeEmUsoException(
//                String.format("Cidade de código %d não pode ser removida, pois está em uso", restauranteId));
//        }
//    }
	

}
