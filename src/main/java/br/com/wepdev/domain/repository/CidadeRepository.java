package br.com.wepdev.domain.repository;

import java.util.List;

import br.com.wepdev.domain.model.Cidade;

public interface CidadeRepository {

	
	List<Cidade> listar();
	
	Cidade buscarPorId(Long id);
	
	Cidade salvarOuAtualizar(Cidade cidade);
	
	void remover(Long id);
	
}
