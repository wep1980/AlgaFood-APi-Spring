package br.com.wepdev.api.openapi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Classe criada para fins de documentação
 */
@ApiModel("Pageable") // Alterando o nome da classe para Pageable na documentação -- http://api.algafood.local:8080/swagger-ui/index.html#
@Setter
@Getter
public class PageableModelOpenApi {


    @ApiModelProperty(example = "0", value = "Número da página (começa em 0)")
    private int page;

    @ApiModelProperty(example = "10", value = "Quantidade de elementos por página")
    private int size;

    @ApiModelProperty(example = "nome,asc", value = "Nome da propriedade para ordenação")
    private List<String> sort;


}
