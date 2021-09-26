package br.com.wepdev.api.DTO.INPUT;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EstadoIdInputDTO {


    @NotNull
    private Long id;
}
