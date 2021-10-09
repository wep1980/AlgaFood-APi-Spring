package br.com.wepdev.api.inputDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class ItemPedidoInputDTO {


    @NotNull
    private Long produtoId;

    @NotNull
    @PositiveOrZero
    private Integer quantidade;

    private String observacao;

}
