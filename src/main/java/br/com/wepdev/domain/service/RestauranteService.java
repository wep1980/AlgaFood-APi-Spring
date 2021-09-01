package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.model.Cidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

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


	//public static final String MSG_ERRO_CADASTRO_COZINHA = "Não existe cadastro de cozinha com código %d";
	public static final String MSG_ERRO_RESTAURANTE_NAO_ENCONTRADO = "Não existe cadastro de restaurante com código %d";
	public static final String MSG_ERRO_RESTAURANTE_USO = "Cidade de código %d não pode ser removida, pois está em uso";


	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CozinhaService cozinhaService;
	
	
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cozinhaService.buscarOuFalhar(cozinhaId);
		
		restaurante.setCozinha(cozinha);
		
		return restauranteRepository.save(restaurante);
	}


//	public void excluir(Long restauranteId) {
//		try {
//			restauranteRepository.deleteById(restauranteId);
//
//		} catch (EmptyResultDataAccessException e) {
//			throw new EntidadeNaoEncontradaException(
//					String.format(MSG_ERRO_RESTAURANTE_NAO_ENCONTRADO, restauranteId));
//
//		} catch (DataIntegrityViolationException e) {
//			throw new EntidadeEmUsoException(
//					String.format(MSG_ERRO_RESTAURANTE_USO, restauranteId));
//		}
//	}

	/**
	 * Optional -> E o tipo de objeto que o findById retorna, que pode ser qualquer entidade.
	 * orElseThrow -> Retorna o objeto que esta dentro do Optional, se nao tiver nada dentro do Optional,
	 * ele lança a excessao
	 */
	public Restaurante buscarOuFalhar(Long restauranteId){
		return restauranteRepository.findById(restauranteId).orElseThrow(() -> new EntidadeNaoEncontradaException(
				String.format(MSG_ERRO_RESTAURANTE_NAO_ENCONTRADO, restauranteId)));
	}
	

}
