package br.com.wepdev.api.inputDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EstadoIdInputDTO {


    @ApiModelProperty(example = "1")
    @NotNull
    private Long id;
}
