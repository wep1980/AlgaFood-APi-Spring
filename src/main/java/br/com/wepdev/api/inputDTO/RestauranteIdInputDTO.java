package br.com.wepdev.api.inputDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RestauranteIdInputDTO {

    @ApiModelProperty(example = "1", required = true)
    @NotNull
    private Long id;

}
