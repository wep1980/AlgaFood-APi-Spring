package br.com.wepdev.api.inputDTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class UsuarioInputDTO {

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

}
