package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.exception.ProdutoNaoEncontradoException;
import br.com.wepdev.domain.exception.UsuarioNaoEncontradoException;
import br.com.wepdev.domain.model.Produto;
import br.com.wepdev.domain.model.Usuario;
import br.com.wepdev.domain.repository.ProdutoRepository;
import br.com.wepdev.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProdutoService {


	@Autowired
	private ProdutoRepository produtoRepository;



	@Transactional
	public Produto salvar(Produto produto) {
		return produtoRepository.save(produto);
	}


	public Produto buscarOuFalhar(Long restauranteId, Long produtoId) {
		return produtoRepository.findById(restauranteId, produtoId)
				.orElseThrow(() -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
	}
}
