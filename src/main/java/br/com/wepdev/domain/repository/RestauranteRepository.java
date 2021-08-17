package br.com.wepdev.domain.repository;

import java.util.List;

import br.com.wepdev.domain.model.Restaurante;

public interface RestauranteRepository {

	
	List<Restaurante> listar();
	
	Restaurante buscarPorId(Long id);
	
	Restaurante salvarOuAtualizar(Restaurante cozinha);
	
	void remover(Restaurante restaurante);
	
}
