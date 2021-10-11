package br.com.wepdev.domain.repository;

import br.com.wepdev.domain.model.FotoProduto;
import br.com.wepdev.domain.model.Restaurante;

import java.math.BigDecimal;
import java.util.List;

/**
 * As aassinaturas dos metodos aqui, sao implementados no RepositoryImpl
 * @author Waldir
 *
 */
public interface ProdutoRepositoryQueries {


	FotoProduto save(FotoProduto foto);

	void delete(FotoProduto foto);

}