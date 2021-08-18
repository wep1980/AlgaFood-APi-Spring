package br.com.wepdev.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.wepdev.domain.model.Restaurante;

public interface RestauranteRepositoryQueries {

	/**
	 * É necessario colocar a assinatura do metodo na Interface RestauranteRepository
	 * 
	 * @param nome
	 * @param taxaFreteInicial
	 * @param taxaFreteFinal
	 * @return
	 */
	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

}