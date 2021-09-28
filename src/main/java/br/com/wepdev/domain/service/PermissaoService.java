package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.PermissaoNaoEncontradaException;
import br.com.wepdev.domain.model.Permissao;
import br.com.wepdev.domain.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PermissaoService {


	@Autowired
	private PermissaoRepository permissaoRepository;



	public Permissao buscarOuFalhar(Long permissaoId) {
		return permissaoRepository.findById(permissaoId)
				.orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
	}

}
