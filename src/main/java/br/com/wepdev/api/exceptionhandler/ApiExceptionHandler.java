package br.com.wepdev.api.exceptionhandler;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Classe global de tratamento de excessao customizada para representação.
 * Todos os metodos da classe que sao Handlers passam por um metodo central handleExceptionInternal() sobrescrito de ResponseEntityExceptionHandler.
 */
// ResponseEntityExceptionHandler -> implementação padrão que trata exceptions internas do Spring
@ControllerAdvice // Permite adicionar exceptions handlers do projeto inteiro
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {



    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> handleInvalidFormatException(InvalidFormatException ex, WebRequest request){

        HttpStatus status = HttpStatus.BAD_REQUEST;
        /**
         * Throwable -> Super tipo de todas as exceptions.
         *
         * ExceptionUtils -> 	<dependency>
         * 			<groupId>org.apache.commons</groupId>
         * 			<artifactId>commons-lang3</artifactId>
         * 			<version>3.1</version>
         * 		</dependency> Dependencia adionada ao pom.xml
         *
         * 	getRootCause() -> metodo que mostra a causa raiz, ele percorre toda a pilha de excessões
         */
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        /**
         * ex.getPath() -> Retorna uma lista de referencia, ou seja uma lista com as propriedades
         * stream() -> Cria um fluxo de reference
         * map(ref -> ref.getFieldName()) -> retornando a stream com o resultado do filedName para cada resuultado dentro Path
         * collect(Collectors.joining(".") -> Reduz os elementos, o coletor concatena os elementos usando o delimitador "." Exp : cozinha.id
         *
         */
        String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));

        Problem problem = Problem.builder()
                .status(status.value())
                .type("http://algofood.com.br/mensagem-incompreensivel")
                .title("Mensagem incompreensível")
                .detail(String.format("A propriedade '%s' recebeu o valor '%s' ," + " que é de um tipo inválido. Corrija e informe um valor compatível com " +
                        "o tipo %s.", path , ex.getValue(), ex.getTargetType().getSimpleName()))
                        // path -> nome do campo da propriedade no qual o valor foi digitado errado
                        // ex.getValue() -> Pega o valor digitado na representação -- ex.getTargetType().getSimpleName() -> Avisa o formato correto que o campo aceita
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }


    /**
     * Metodo de exception usado para erro de sintaxe na requisição (POSTMAN)
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        /**
         * Enumeracao onde fica as constantes dos tipos de problemas gerados pelas exceptions, novos problemas devem ser colocados dentro dela
         * Nela possue descrições do titulo e da uri, que é um tipo de URL
         */
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição esta inválido. Verifique erro de sintaxe."; // Pega a informacao do detalhe da mensagem

        Problem problem = createProblemBuilder(status, problemType, detail) // Antes do builder podemos customizar mais propriedades adicionais
                .build(); // Ao dar o build(), a instancia de Problem e criada

        /**
         * ex -> Exception
         * body -> problem, e o corpo onde contem as propriedades
         * new HttpHeaders() -> Instancia vazia de um Header, sem cabeçalho customizado na resposta
         * status -> HttpStatus.NOT_FOUND -> status de nao encontrado, 404
         * WebRequest resquest -> O Spring ja passa automaticamente , representa uma requisição WEB
         *
         * Não é mais nessario instanciar a Classe problema aqui, pois ja esta sendo instanciada no metodo handleExceptionInternal
         */
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);

    }

    /**
     * Metodo que trata excessoes customizado
     * Quando a EntidadeNaoEncontradaException for tratada, esse metodo vai ser automaticamente chamado pelo Spring, passando a
     * exception que foi lançada.
     *
     * Os erros que aparecem na representação (POSTMAN) estao customizados
     */
    @ExceptionHandler(EntidadeNaoEncontradaException.class) // WebRequest resquest -> É passado automaticamente pelo Spring implicitamente, ele so foi colocado explicitamente
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex , WebRequest resquest){

        HttpStatus status = HttpStatus.NOT_FOUND; //Entidade nao encontrada retorna sempre um NOT_FOUND 404
        String detail = ex.getMessage(); // Pega a informacao do detalhe da mensagem

        /**
         * Enumeracao onde fica as constantes dos tipos de problemas gerados pelas exceptions, novos problemas devem ser colocados dentro dela
         * Nela possue descrições do titulo e da uri, que é um tipo de URL
         */
        ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;

        Problem problem = createProblemBuilder(status, problemType, detail) // Antes do builder podemos customizar mais propriedades adicionais
                .build(); // Ao dar o build(), a instancia de Problem e criada

          // Forma de colocar informações sem utilizar o metedo acima
//        Problem problem = Problem.builder() // Intanciando um problem com o construtor do lombok @Builder, implementando o corpo da Exception de acordo com a RFC 7807
//                .status(status.value())
//                .type("https://algafood.com.br/entidade-nao-encontrada") // Url da pagina que esta documentada com o tipo do problema e uma possivel solução, essa pagina nao precisa exatamente existir, mas o ideal é q exista
//                .title("Entidade não encontrada")
//                .detail(ex.getMessage())
//                .build(); // Ao dar o build(), a instancia de Problem e criada
        /**
         * ex -> Exception
         * body -> problem, e o corpo onde contem as propriedades
         * new HttpHeaders() -> Instancia vazia de um Header, sem cabeçalho customizado na resposta
         * status -> HttpStatus.NOT_FOUND -> status de nao encontrado, 404
         * WebRequest resquest -> O Spring ja passa automaticamente , representa uma requisição WEB
         *
         * Não é mais nessario instanciar a Classe problema aqui, pois ja esta sendo instanciada no metodo handleExceptionInternal
         */
         return handleExceptionInternal(ex, problem, new HttpHeaders(), status, resquest);
    }


    /**
     * Metodo que trata excessoes customizado
     * Quando a NegocioException for tratada , esse metodo vai ser automaticamente chamado pelo Spring, passando a
     * exception que foi lançada.
     * @param
     * @return
     */
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex , WebRequest resquest){

        HttpStatus status = HttpStatus.BAD_REQUEST; //Entidade nao encontrada retorna sempre um BAD_REQUEST 400
        String detail = ex.getMessage(); // Pega a informacao do detalhe da mensagem

        /**
         * Enumeracao onde fica as constantes dos tipos de problemas gerados pelas exceptions, novos problemas devem ser colocados dentro dela
         * Nela possue descrições do titulo e da uri, que é um tipo de URL
         */
        ProblemType problemType = ProblemType.ERRO_NEGOCIO;

        Problem problem = createProblemBuilder(status, problemType, detail)  // Antes do builder podemos customizar mais propriedades adicionais
                .build(); // Ao dar o build(), a instancia de Problem e criada

        /**
         * ex -> Exception
         * body -> problem , e o corpo onde contem as propriedades
         * new HttpHeaders() -> Instancia vazia de um Header, sem cabeçalho customizado na resposta
         * status -> HttpStatus.BAD_REQUEST -> 400 , erro do cliente
         * WebRequest resquest -> O Spring ja passa automaticamente , representa uma requisição WEB
         *
         * Não é mais nessario instanciar a Classe problema aqui, pois ja esta sendo instanciada no metodo handleExceptionInternal
         */
        return handleExceptionInternal(ex , problem, new HttpHeaders(), status, resquest);
    }


    /**
     * Metodo que trata excessoes customizado
     * Quando a EntidadeEmUsoException for tratada , esse metodo vai ser automaticamente chamado pelo Spring, passando a
     * exception que foi lançada.
     * @param
     * @return
     */
    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex , WebRequest resquest){

        HttpStatus status = HttpStatus.CONFLICT; //Entidade nao encontrada retorna sempre um CONFLICT 409
        String detail = ex.getMessage(); // Pega a informacao do detalhe da mensagem

        /**
         * Enumeracao onde fica as constantes dos tipos de problemas gerados pelas exceptions, novos problemas devem ser colocados dentro dela
         * Nela possue descrições do titulo e da uri, que é um tipo de URL
         */
        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;

        Problem problem = createProblemBuilder(status, problemType, detail)  // Antes do builder podemos customizar mais propriedades adicionais
                .build(); // Ao dar o build(), a instancia de Problem e criada

        /**
         * ex -> Exception
         * body -> problem , a mensagem da exception
         * new HttpHeaders() -> Instancia vazia de um Header, sem cabeçalho customizado na resposta
         * status -> HttpStatus.BAD_REQUEST -> status de conflito, 403
         * WebRequest resquest -> O Spring ja passa automaticamente , representa uma requisição WEB
         *
         * Não é mais nessario instanciar a Classe problema aqui, pois ja esta sendo instanciada no metodo handleExceptionInternal
         */
        return handleExceptionInternal(ex , problem, new HttpHeaders(), status, resquest);
    }


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
            body = Problem.builder()
                    .title(status.getReasonPhrase()) // Descreve o status que esta sendo retornado na resposta
                    .status(status.value()) // Pega o HTTP.Status que é uma enumercao
                    .build();

        } else if(body instanceof String){ // Se o corpo(body) for uma instancia de uma String
            body = Problem.builder()
                    .title((String) body) // Faz o cast do Object(body) para String
                    .status(status.value()) // Pega o HTTP.Status que é uma enumercao
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * Metodo auxiliar que ajuda na hora de criar um tipo de problema , que pode ser uma EntidadeNaoEncontrada, EntidadeEmUso ...etc
     * @param status -> Retorna um HttpStatus
     * @param problemType -> Retorna uma instancia do enum ProblemType, classe criada com os tipos de problemas que podem ocorrer nas exceptions
     * @param detail -> Mensagem de detalhe
     * @return // Retorna a instancia de um builder de um problema, nao uma instancia do Problem,
     * Na classe Problem e utilizado o @Builder do lombok, que criar um construtor de uma forma diferente.
     * Problem.ProblemBuilder -> cria a classe ProblemBuilder dentro de Problem
     */
    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail){

        return Problem.builder()
                .status(status.value()) // Pega o valor do status
                .type(problemType.getUri()) // Pega o valor da uri que esta dentro do enum ProblemType
                .title(problemType.getTitle()) // Pega o valor do title que esta dentro do enum ProblemType
                .detail(detail); // Passa do detalhe do problema
              //.build(); -> O Builde nao e feito pq é para ser construido nesse momento a instancia do problem
    }



}
