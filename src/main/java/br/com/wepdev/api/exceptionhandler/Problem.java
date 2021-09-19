package br.com.wepdev.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Classe que customiza as informacoes dos erros que vao aparecer na representção (POSTMAN)
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // So inclue na representação( POSTMAN ) se o valor da propriedade nao estiver nulo
@Getter
@Builder // Construtor da classe, uma forma de diferente de instanciar a classe, construtor utilizado no controller da Cozinha
public class Problem {

    private Integer status;
    private LocalDateTime timestamp;
    private String type;
    private String title;
    private String detail;
    private String userMessage;

    /**
     * Lista de propriedades(objetos) onde vai ter o nome da propriedade que esta o erro
     */
    private List<Objeto> objetos;



    // Classe static so para os campos
    @Getter
    @Builder
    public static class Objeto {

        private String name;
        private String userMessage;
    }


}
