package br.com.wepdev.api.DTOentrada;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GrupoInputDTO {

    @ApiModelProperty(example = "Gerente", required = true)
    @NotBlank
    private String nome;


}
