package br.com.wepdev.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.bytecode.enhance.spi.interceptor.EnhancementHelper;

import java.time.LocalDateTime;

/**
 * Classe que customiza as informacoes dos erros que vao aparecer na representção (POSTMAN)
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // So inclue na representação( POSTMAN ) se o valor da propriedade nao estiver nulo
@Getter
@Builder // Construtor da classe, uma forma de diferente de instanciar a classe, construtor utilizado no controller da Cozinha
public class Problem {

    private Integer status;
    private String type;
    private String title;
    private String detail;

}
