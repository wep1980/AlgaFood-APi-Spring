package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.RestauranteNaoEncontradoException;
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
import org.springframework.transaction.annotation.Transactional;


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



	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cozinhaService.buscarOuFalhar(cozinhaId);
		
		restaurante.setCozinha(cozinha);
		
		return restauranteRepository.save(restaurante);
	}

	/**
	 * Metodo que ativa um restaurante
	 * @param restauranteId
	 */
	@Transactional
	public void ativar(Long restauranteId){
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);

        /*
         Nao e preciso salvar o restaurante, pois essa instancia de restaurante esta no estado de gerenciada pelo JPA, ou seja
         qualquer modificacao que for feita durante essa transação ela sera sincronizada com o banco de dados,
         ou seja e feito um update
         */
		//restauranteAtual.setAtivo(true);

		// Metodo criado na Entidade Restaurante, deixa mais legivel e se no futuro for necessario implementar mais alguma coisa, dentro desse metodo fica mais facil
		restauranteAtual.ativar();
	}


	/**
	 * Metodo que inativa um restaurante
	 * @param restauranteId
	 */
	@Transactional
	public void inativar(Long restauranteId){
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);

        /*
         Nao e preciso salvar o restaurante, pois essa instancia de restaurante esta no estado de gerenciada pelo JPA, ou seja
         qualquer modificacao que for feita durante essa transação ela sera sincronizada com o banco de dados,
         ou seja e feito um update
         */
		//restauranteAtual.setAtivo(false);

		// Metodo criado na Entidade Restaurante, deixa mais legivel e se no futuro for necessario implementar mais alguma coisa, dentro desse metodo fica mais facil
		restauranteAtual.inativar();
	}


	/**
	 * Optional -> E o tipo de objeto que o findById retorna, que pode ser qualquer entidade.
	 * orElseThrow -> Retorna o objeto que esta dentro do Optional, se nao tiver nada dentro do Optional,
	 * ele lança a excessao
	 */
	public Restaurante buscarOuFalhar(Long restauranteId){
		return restauranteRepository.findById(restauranteId).orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}
	

}
