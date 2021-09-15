package br.com.wepdev.core.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

/**
 * Classe customizada de exception que carrega o bindingResult, onde estao os erros de validação
 */
@AllArgsConstructor // Lombok, cria o construtor recebendo todos os argumentos
@Getter
public class ValidacaoException extends RuntimeException{


    private BindingResult bindingResult;
}
