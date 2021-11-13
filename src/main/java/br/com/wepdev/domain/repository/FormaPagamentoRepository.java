package br.com.wepdev.domain.repository;

import br.com.wepdev.domain.model.FormaPagamento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface FormaPagamentoRepository extends CustomJpaRepository<FormaPagamento, Long> {


    //Pegando a ultima data de atualização
    @Query("select  max(dataAtualizacao) from FormaPagamento")
    OffsetDateTime getDataUltimaAtualizacao();


    @Query("select dataAtualizacao from FormaPagamento where id = :formaPagamentoId")
    OffsetDateTime getDataAtualizacaoById(Long formaPagamentoId);


}
