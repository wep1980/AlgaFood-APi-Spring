package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.CidadeNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.model.Estado;
import br.com.wepdev.domain.repository.CidadeRepository;
import br.com.wepdev.domain.repository.EstadoRepository;

@Service
public class CidadeService {


	//public static final String MSG_ERRO_CIDADE_NAO_ENCONTRADA = "Não existe um cadastro de cidade com código %d";
	public static final String MSG_ERRO_CIDADE_USO = "Cidade de código %d não pode ser removida, pois está em uso";


	@Autowired
    private CidadeRepository cidadeRepository;
    
    @Autowired
    private EstadoRepository estadoRepository;

	@Autowired
	private EstadoService estadoService;
    
    
    
	public Cidade salvar(Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();

		Estado estado = estadoService.buscarOuFalhar(estadoId);
		
		cidade.setEstado(estado);
		
		return cidadeRepository.save(cidade);
	}

	
	public void excluir(Long cidadeId) {
		try {
			cidadeRepository.deleteById(cidadeId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(cidadeId);
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
				String.format(MSG_ERRO_CIDADE_USO, cidadeId));
		}
	}

	/**
	 * Optional -> E o tipo de objeto que o findById retorna, que pode ser qualquer entidade.
	 * orElseThrow -> Retorna o objeto que esta dentro do Optional, se nao tiver nada dentro do Optional,
	 * ele lança a excessao
	 */
	public Cidade buscarOuFalhar(Long cidadeId){
		return cidadeRepository.findById(cidadeId).orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
	}
}
