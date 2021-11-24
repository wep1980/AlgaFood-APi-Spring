package br.com.wepdev.api.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EstadoDTO {


    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "Minas gerais")
    private String nome;


}
