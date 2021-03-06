package br.com.wepdev.domain.repository;

import br.com.wepdev.domain.model.FotoProduto;
import br.com.wepdev.domain.model.Produto;
import br.com.wepdev.domain.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoRepositoryQueries {


    @Query("from Produto where restaurante.id = :restaurante and id = :produto")
    Optional<Produto> findById(@Param("restaurante") Long restauranteId, @Param("produto") Long produtoId);


    List<Produto> findTodosByRestaurante(Restaurante restaurante);


    @Query("from Produto p where p.ativo = true and p.restaurante = :restaurante")
    List<Produto> findAtivosByRestaurante(Restaurante restaurante);


    /**
     * A foto sera sempre buscada pelo restauranteId e produtoId pois o mesmo produto pode existir em mais de um restaurante.
     *
     * Como dentro da tabela Produto existe um Restaurante e dentro da tabela FotoProduto existe um produto e possivel fazer um join
     * para buscar restauranteId e produtoId
     */
    @Query("select f from FotoProduto f join f.produto p where p.restaurante.id = :restauranteId and f.produto.id = :produtoId")
    Optional<FotoProduto> findFotoById(Long restauranteId, Long produtoId);
	
}
