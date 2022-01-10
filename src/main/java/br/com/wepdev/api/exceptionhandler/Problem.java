package br.com.wepdev.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;


/**
 * Classe que customiza as informacoes dos erros que vão aparecer na representção (POSTMAN)
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // So inclue na representação( POSTMAN ) se o valor da propriedade nao estiver nulo
@Getter
@Builder // Construtor da classe, uma forma de diferente de instanciar a classe, construtor utilizado no controller da Cozinha
@ApiModel("Problema") // Customizando o nome da classe na documentação
public class Problem {


    @ApiModelProperty(example = "400", position = 1)
    private Integer status;

    @ApiModelProperty(example = "2022-01-10T12:12:53.465964Z", position = 2)
    private OffsetDateTime timestamp;

    @ApiModelProperty(example = "https://algafood.com.br/dados-invalidos", position = 3)
    private String type;

    @ApiModelProperty(example = "Dados inválidos", position = 4)
    private String title;

    @ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente." , position = 5)
    private String detail;

    @ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.", position = 6)
    private String userMessage;

    /**
     * Lista de propriedades(objetos) onde vai ter o nome da propriedade que esta o erro
     */
    @ApiModelProperty(value = "Lista de objetos ou campos que geraram o erro (opcional)" , position = 7)
    private List<Objeto> objetos;


    // Classe static so para os campos
    @ApiModel("ObjetoProblema")
    @Getter
    @Builder
    public static class Objeto {

        @ApiModelProperty(example = "preço")
        private String name;

        @ApiModelProperty(example = "preço é obrigatório")
        private String userMessage;
    }


}
