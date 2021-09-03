package br.com.wepdev.api.exceptionhandler;

import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

/**
 * Classe global de tratamento de excessao customizada para representação
 */
@ControllerAdvice // Permite adicionar exceptions handlers do projeto inteiro
public class ApiExceptionHandler {


    /**
     * Metodo que trata excessoes dentro do proprio controlador
     * Quando a EntidadeNaoEncontradaException for tratada esse metodo vai ser automaticamente chamado pelo Spring, passando a
     * exception que foi lançada.
     *
     * Os erros que aparecem na representação (POSTMAN) estao customizados
     */
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> tratarEntidadeNaoEncontradoException(EntidadeNaoEncontradaException e){

        /**
         * Construtor feito com o @Builder do lombok.
         * Problema e a classe responsavel pelos atributos de erros mostrados na representação (POSTMAN)
         */
        Problema problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
    }



    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> tratarNegocioException(NegocioException e){

        /**
         * Construtor feito com o @Builder do lombok.
         * Problema e a classe responsavel pelos atributos de erros mostrados na representação (POSTMAN)
         */
        Problema problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);

    }

    /**
     * Metodo de erro ao tentar enviar um tipo de midia diferente de JSON
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> tratarHttpMediaTypeNotSupportedException(){

        /**
         * Construtor feito com o @Builder do lombok.
         * Problema e a classe responsavel pelos atributos de erros mostrados na representação (POSTMAN)
         */
        Problema problema = Problema.builder()
                .dataHora(LocalDateTime.now())
                .mensagem("O tipo de mídia não é aceito.")
                .build();

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(problema); // Retorna 415 -> Nao suporta o tipo de Midia

    }













}
