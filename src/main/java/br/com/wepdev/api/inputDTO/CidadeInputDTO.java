package br.com.wepdev.api.inputDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CidadeInputDTO {


    @ApiModelProperty(example = "Uberlandia", required = true)
    @NotBlank
    private String nome;

    @Valid
    @NotNull
    private EstadoIdInputDTO estado;

}
