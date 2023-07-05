package br.com.wepdev.api.DTOentrada;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteInputDTO {

    @NotBlank
    private String nome;

    @NotNull
    @PositiveOrZero //O valor tem ser positivo ou zero 0
    private BigDecimal taxaFrete;

    @Valid // Valida os campos que estao em CozinhaIdINPUT
    @NotNull // Nao pode ser nulo
    private CozinhaIdInputDTO cozinha;

    @Valid // Valida os campos que estao em enderecoEntradaDTO
    @NotNull // O endereço nao pode ser nulo
    private EnderecoInputDTO endereco;

}
