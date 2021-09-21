package br.com.wepdev.api.DTO.INPUT;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteINPUT {

    @NotBlank
    private String nome;

    @NotNull
    @PositiveOrZero //O valor tem ser positivo ou zero 0
    private BigDecimal taxaFrete;

    @Valid
    @NotNull
    private CozinhaIdINPUT cozinha;

}
