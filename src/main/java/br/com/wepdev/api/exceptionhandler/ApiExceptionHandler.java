package br.com.wepdev.api.exceptionhandler;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * Classe global de tratamento de excessao customizada para representação.
 * Todos os metodos da classe que sao Handlers passam por um metodo central handleExceptionInternal() sobrescrito de ResponseEntityExceptionHandler.
 */
// ResponseEntityExceptionHandler -> implementação padrão que trata exceptions internas do Spring
@ControllerAdvice // Permite adicionar exceptions handlers do projeto inteiro
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * Metodo que trata excessoes customizado
     * Quando a EntidadeNaoEncontradaException for tratada, esse metodo vai ser automaticamente chamado pelo Spring, passando a
     * exception que foi lançada.
     *
     * Os erros que aparecem na representação (POSTMAN) estao customizados
     */
    @ExceptionHandler(EntidadeNaoEncontradaException.class) // WebRequest resquest -> É passado automaticamente pelo Spring implicitamente, ele so foi colocado explicitamente
    public ResponseEntity<?> tratarEntidadeNaoEncontradoException(EntidadeNaoEncontradaException ex , WebRequest resquest){
        /**
         * ex -> Exception
         * body -> getMessage , a mensagem da exception
         * new HttpHeaders() -> Instancia vazia de um Header, sem cabeçalho customizado na resposta
         * status -> HttpStatus.NOT_FOUND -> status de nao encontrado, 404
         * WebRequest resquest -> O Spring ja passa automaticamente , representa uma requisição WEB
         *
         * Não é mais nessario instanciar a Classe problema aqui, pois ja esta sendo instanciada no metodo handleExceptionInternal
         */
         return handleExceptionInternal(ex , ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, resquest);

        /**
         * Construtor feito com o @Builder do lombok.
         * Problema e a classe responsavel pelos atributos de erros mostrados na representação (POSTMAN)
         */
//        Problema problema = Problema.builder()
//                .dataHora(LocalDateTime.now())
//                .mensagem(e.getMessage())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
    }


    /**
     * Metodo que trata excessoes customizado
     * Quando a NegocioException for tratada , esse metodo vai ser automaticamente chamado pelo Spring, passando a
     * exception que foi lançada.
     * @param e
     * @return
     */
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> tratarNegocioException(NegocioException ex ,  WebRequest resquest){

        /**
         * ex -> Exception
         * body -> getMessage , a mensagem da exception
         * new HttpHeaders() -> Instancia vazia de um Header, sem cabeçalho customizado na resposta
         * status -> HttpStatus.BAD_REQUEST -> 400 , erro do cliente
         * WebRequest resquest -> O Spring ja passa automaticamente , representa uma requisição WEB
         *
         * Não é mais nessario instanciar a Classe problema aqui, pois ja esta sendo instanciada no metodo handleExceptionInternal
         */
        return handleExceptionInternal(ex , ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, resquest);

        /**
         * Construtor feito com o @Builder do lombok.
         * Problema e a classe responsavel pelos atributos de erros mostrados na representação (POSTMAN)
         */
//        Problema problema = Problema.builder()
//                .dataHora(LocalDateTime.now())
//                .mensagem(e.getMessage())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);

    }


    /**
     * Metodo que trata excessoes customizado
     * Quando a EntidadeEmUsoException for tratada , esse metodo vai ser automaticamente chamado pelo Spring, passando a
     * exception que foi lançada.
     * @param e
     * @return
     */
    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> tratarEntidadeEmUsoException(EntidadeEmUsoException ex ,  WebRequest resquest){

        /**
         * ex -> Exception
         * body -> getMessage , a mensagem da exception
         * new HttpHeaders() -> Instancia vazia de um Header, sem cabeçalho customizado na resposta
         * status -> HttpStatus.BAD_REQUEST -> status de conflito, 403
         * WebRequest resquest -> O Spring ja passa automaticamente , representa uma requisição WEB
         *
         * Não é mais nessario instanciar a Classe problema aqui, pois ja esta sendo instanciada no metodo handleExceptionInternal
         */
        return handleExceptionInternal(ex , ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, resquest);

        /**
         * Construtor feito com o @Builder do lombok.
         * Problema e a classe responsavel pelos atributos de erros mostrados na representação (POSTMAN)
         */
//        Problema problema = Problema.builder()
//                .dataHora(LocalDateTime.now())
//                .mensagem(e.getMessage())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(problema);

    }


    /**
     * Ao extender ResponseEntityExceptionHandler nao e mais necessario tratar exceptions interna do Spring
     *
     * Metodo de erro ao tentar enviar um tipo de midia diferente de JSON
     * @return
     */
//    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//    public ResponseEntity<?> tratarHttpMediaTypeNotSupportedException(){
//
//        /**
//         * Construtor feito com o @Builder do lombok.
//         * Problema e a classe responsavel pelos atributos de erros mostrados na representação (POSTMAN)
//         */
//        Problema problema = Problema.builder()
//                .dataHora(LocalDateTime.now())
//                .mensagem("O tipo de mídia não é aceito.")
//                .build();
//
//        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(problema); // Retorna 415 -> Nao suporta o tipo de Midia
//
//    }

    /**
     * Metodo padrao do ResponseEntityExceptionHandler sobrestrito com customização.
     *
     * Obs -> Em outros locais podem estar sendo usado o body desse método, então ao sobrescrever precisa tomar cuidado
     *
     * @param ex
     * @param body
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if(body == null){ // Retorna o corpo(body) com o getReasonPhrase() do status
            body = Problema.builder()
                    .dataHora(LocalDateTime.now())
                    .mensagem(status.getReasonPhrase()) // Descreve o status que esta sendo retornado na resposta
                    .build();

        } else if(body instanceof String){ // Se o corpo(body) for uma instancia de uma String
            body = Problema.builder()
                    .dataHora(LocalDateTime.now())
                    .mensagem((String) body) // Faz o cast do Object(body) para String
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }






}
