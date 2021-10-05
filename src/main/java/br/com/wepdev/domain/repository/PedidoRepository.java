package br.com.wepdev.domain.repository;

import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.infrastructure.repository.CustomJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PedidoRepository extends CustomJpaRepository<Pedido, Long> {


    /**
     * Consulta JPQL.
     * Resolvendo o problema do N +1
     * @return
     */
    @Query("from Pedido p join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha")
    List<Pedido> findAll();


    //@Query("from Pedido where codigo = :codigo")
    Optional<Pedido> findByCodigo(String codigo);


}
