package br.com.wepdev.api.exceptionhandler;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Classe que customiza as informacoes dos erros que vao aparecer na representção (POSTMAN)
 */
@Getter
@Builder // Construtor da classe, uma forma de diferente de instanciar a classe, construtor utilizado no controller da Cozinha
public class Problema {

    private LocalDateTime dataHora;
    private String mensagem;
}
