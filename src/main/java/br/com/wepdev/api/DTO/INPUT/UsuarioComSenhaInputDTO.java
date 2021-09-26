package br.com.wepdev.api.DTO.INPUT;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class UsuarioComSenhaInputDTO extends UsuarioInputDTO{

    @NotBlank
    private String senha;

}
