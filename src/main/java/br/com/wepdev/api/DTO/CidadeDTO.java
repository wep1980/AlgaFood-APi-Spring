package br.com.wepdev.api.DTO;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

//@ApiModel(value = "Cidade", description = "Representa uma cidade") // Alterando o nome do modelo somente na documentação
@Getter
@Setter
public class CidadeDTO {


    @ApiModelProperty(example = "1")
    private Long id;

    @ApiModelProperty(example = "Uberlandia")
    private String nome;

    private EstadoDTO estado;
}
