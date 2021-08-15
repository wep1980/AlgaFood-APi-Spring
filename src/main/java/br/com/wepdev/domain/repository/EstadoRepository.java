package br.com.wepdev.domain.repository;

import java.util.List;

import br.com.wepdev.domain.model.Estado;

public interface EstadoRepository {

	
	List<Estado> listar();
	
	Estado buscarPorId(Long id);
	
	Estado salvarOuAtualizar(Estado estado);
	
	void remover(Estado estado);
	
}
