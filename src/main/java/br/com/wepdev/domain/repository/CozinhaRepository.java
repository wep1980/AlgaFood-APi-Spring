package br.com.wepdev.domain.repository;

import java.util.List;

import br.com.wepdev.domain.model.Cozinha;

public interface CozinhaRepository {

	
	List<Cozinha> todas();
	
	Cozinha porId(Long id);
	
	Cozinha adicionar(Cozinha cozinha);
	
	void remover(Cozinha cozinha);
	
}
