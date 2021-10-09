package br.com.wepdev.api.inputDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class PedidoInputDTO {

    @Valid
    @NotNull
    private RestauranteIdInputDTO restaurante;

    @Valid
    @NotNull
    private EnderecoInputDTO enderecoEntrega;

    @Valid
    @NotNull
    private FormaPagamentoIdInputDTO formaPagamento;

    @Valid
    @Size(min = 1) // Obrigatorio ter no minimo 1 elemento na lista
    @NotNull
    private List<ItemPedidoInputDTO> itens;

}
