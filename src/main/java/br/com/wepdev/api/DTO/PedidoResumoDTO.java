package br.com.wepdev.api.DTO;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

// Apos a configuracao do Squiggly se anotacao estiver ativa o Squiggly nao funcionara
//@JsonFilter("pedidoFilter") // Anotacao que permite ao consumidor da api retornar apenas os campos desejados na serializacao dos objetos
@Getter
@Setter
public class PedidoResumoDTO {

    //private Long id;
    private String codigo;
    private BigDecimal subtotal;
    private BigDecimal taxaFrete;
    private BigDecimal valorTotal;
    private String status;
    private OffsetDateTime dataCriacao;
    private RestauranteResumoDTO restaurante;
    private UsuarioDTO cliente;

}
