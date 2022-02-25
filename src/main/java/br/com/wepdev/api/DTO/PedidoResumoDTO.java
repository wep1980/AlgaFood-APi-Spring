package br.com.wepdev.api.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

// Apos a configuracao do Squiggly se anotacao estiver ativa o Squiggly nao funcionara
//@JsonFilter("pedidoFilter") // Anotacao que permite ao consumidor da api retornar apenas os campos desejados na serializacao dos objetos
@Getter
@Setter
public class PedidoResumoDTO {

    //private Long id;

    @ApiModelProperty(example = "f9981ca4-5a5e-4da3-af04-933861df3e55")
    private String codigo;

    @ApiModelProperty(example = "298.90")
    private BigDecimal subtotal;

    @ApiModelProperty(example = "10.00")
    private BigDecimal taxaFrete;

    @ApiModelProperty(example = "308.90")
    private BigDecimal valorTotal;

    @ApiModelProperty(example = "CRIADO")
    private String status;

    @ApiModelProperty(example = "2019-12-01T20:34:04Z")
    private OffsetDateTime dataCriacao;

    private RestauranteResumoDTO restaurante;

    //private UsuarioDTO cliente;

    private String nomeCliente;

}
