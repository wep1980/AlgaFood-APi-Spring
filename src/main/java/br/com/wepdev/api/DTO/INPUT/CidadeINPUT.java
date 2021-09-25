package br.com.wepdev.api.DTO.INPUT;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CidadeINPUT {

    @NotBlank
    private String nome;

    @Valid
    @NotNull
    private EstadoIdINPUT estado;

}
