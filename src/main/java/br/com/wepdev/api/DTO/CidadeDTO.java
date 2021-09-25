package br.com.wepdev.api.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeDTO {

    private Long id;
    private String nome;
    private EstadoDTO estado;
}
