package br.com.wepdev.domain.repository;

import java.util.List;

import br.com.wepdev.domain.model.Cozinha;

public interface CozinhaRepository {

	
	List<Cozinha> listar();
	
	Cozinha buscarPorId(Long id);
	
	Cozinha salvarOuAtualizar(Cozinha cozinha);
	
	void remover(Long id);
	
}
