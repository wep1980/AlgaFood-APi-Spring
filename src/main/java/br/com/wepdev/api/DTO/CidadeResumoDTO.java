package br.com.wepdev.api.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeResumoDTO {

    @ApiModelProperty(example = "1", required = true)
    private Long id;

    @ApiModelProperty(example = "Uberlândia")
    private String nome;

    @ApiModelProperty(example = "Minas Gerais")
    private String estado;
}
