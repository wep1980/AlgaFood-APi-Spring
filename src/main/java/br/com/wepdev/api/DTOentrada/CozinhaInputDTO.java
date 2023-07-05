package br.com.wepdev.api.DTOentrada;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CozinhaInputDTO {

    @ApiModelProperty(example = "Brasileira", required = true)
    @NotBlank
    @NotBlank
    private String nome;
}
