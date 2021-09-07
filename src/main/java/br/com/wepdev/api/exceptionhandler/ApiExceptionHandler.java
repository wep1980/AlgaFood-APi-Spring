package br.com.wepdev.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

/**
 * Classe global de tratamento de excessao customizada para representação.
 * Todos os metodos da classe que sao Handlers passam por um metodo central handleExceptionInternal() sobrescrito de ResponseEntityExceptionHandler.
 */
// ResponseEntityExceptionHandler -> implementação padrão que trata exceptions internas do Spring
@ControllerAdvice // Permite adicionar exceptions handlers do projeto inteiro
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. " +
            "Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";


//    /**
//     *
//     * @param ex
//     * @param request
//     * @return
//     */
//    @ExceptionHandler(InvalidFormatException.class)
//    public ResponseEntity<?> handleInvalidFormatException(InvalidFormatException ex, WebRequest request){
//
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        /**
//         * Throwable -> Super tipo de todas as exceptions.
//         *
//         * ExceptionUtils -> 	<dependency>
//         * 			<groupId>org.apache.commons</groupId>
//         * 			<artifactId>commons-lang3</artifactId>
//         * 			<version>3.1</version>
//         * 		</dependency> Dependencia adionada ao pom.xml
//         *
//         * 	getRootCause() -> metodo que mostra a causa raiz, ele percorre toda a pilha de excessões
//         */
//        //Throwable rootCause = ExceptionUtils.getRootCause(ex);
//
//        /**
//         * ex.getPath() -> Retorna uma lista de referencia, ou seja uma lista com as propriedades
//         * stream() -> Cria um fluxo de reference
//         * map(ref -> ref.getFieldName()) -> retornando a stream com o resultado do filedName para cada resuultado dentro Path
//         * collect(Collectors.joining(".") -> Reduz os elementos, o coletor concatena os elementos usando o delimitador "." Exp : cozinha.id
//         *
//         */
//        String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
//        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
//
//        String detail = String.format( "A propriedade '%s' recebeu o valor '%s' ," + " que é de um tipo inválido. Corrija e informe um valor compatível com " +
//                       "o tipo %s.", path , ex.getValue(), ex.getTargetType().getSimpleName()); // Pega a informacao do detalhe da mensagem
//
//        Problem problem = createProblemBuilder(status, problemType, detail) // Antes do builder podemos customizar mais propriedades adicionais
//                .build(); // Ao dar o build(), a instancia de Problem e cria
//
////        Problem problem = Problem.builder()
////                .status(status.value())
////                .type("http://algofood.com.br/mensagem-incompreensivel")
////                .title("Mensagem incompreensível")
////                .detail(String.format("A propriedade '%s' recebeu o valor '%s' ," + " que é de um tipo inválido. Corrija e informe um valor compatível com " +
////                        "o tipo %s.", path , ex.getValue(), ex.getTargetType().getSimpleName()))
////                        // path -> nome do campo da propriedade no qual o valor foi digitado errado
////                        // ex.getValue() -> Pega o valor digitado na representação -- ex.getTargetType().getSimpleName() -> Avisa o formato correto que o campo aceita
////                .build();
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
//    }
//
//    /**
//     * Metodo de excessao gerado ao colocar uma propriedade na representação que não existe
//     * Capturando erro conforme o metodo mais detalhado acima
//     * @param ex
//     * @param request
//     * @return
//     */
//    @ExceptionHandler(UnrecognizedPropertyException.class)
//    public ResponseEntity<?> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex, WebRequest request){
//
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//        /**
//         * ex.getPath() -> Retorna uma lista de referencia, ou seja uma lista com as propriedades
//         * stream() -> Cria um fluxo de reference
//         * map(ref -> ref.getFieldName()) -> retornando a stream com o resultado do filedName para cada resuultado dentro Path
//         * collect(Collectors.joining(".") -> Reduz os elementos, o coletor concatena os elementos usando o delimitador "." Exp : cozinha.id
//         *
//         */
//        String path = ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
//
//        String detail = String.format("A propriedade '%s' não existe.", path ); // Pega a informacao do detalhe da mensagem
//        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
//
//        Problem problem = createProblemBuilder(status, problemType, detail) // Antes do builder podemos customizar mais propriedades adicionais
//                .build(); // Ao dar o build(), a instancia de Problem e cria
//
////        Problem problem = Problem.builder()
////                .status(status.value())
////                .type("http://algofood.com.br/mensagem-incompreensivel")
////                .title("Mensagem incompreensível")
////                .detail(detail)
////                .build();
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
//    }


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
         * Throwable ->  Super tipo de todas as exceptions
         * ExceptionUtils -> metodo do pacote commons-lang3, dependencia adicionada no pom.xml
         * getRootCause(ex) -> metodo que mostra a causa raiz, ele percorre toda a pilha de excessões
         */
        Throwable rootCause = ExceptionUtils.getRootCause(ex); // Pega a causa da exception na raiz, na pilha das exceptions

        if(rootCause instanceof InvalidFormatException){
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status,request);
        } else if(rootCause instanceof PropertyBindingException){ // PropertyBindingException trata IgnoredPropertyException e tambem UnrecognizedPropertyException
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }

        /**
         * Enumeracao onde fica as constantes dos tipos de problemas gerados pelas exceptions, novos problemas devem ser colocados dentro dela
         * Nela possue descrições do titulo e da uri, que é um tipo de URL
         */
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição esta inválido. Verifique erro de sintaxe."; // Pega a informacao do detalhe da mensagem

        Problem problem = createProblemBuilder(status, problemType, detail) // Antes do builder podemos customizar mais propriedades adicionais
                .mensagemParaUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL) // Mensagem que pode ser utilizada para um usuario final
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
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Metodo que captura as a exceptions IgnoredPropertyException e UnrecognizedPropertyException.
     *
     * IgnoredPropertyException -> execption gerada ao adicionar uma propriedade qye nao existe na representação
     *
     * UnrecognizedPropertyException -> execption gerada ao adicionar uma propriedade que existe na representação porem esta anotada com @JsonIgnore na sua classe
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        // Criei o método joinPath para reaproveitar em todos os métodos que precisam
        // concatenar os nomes das propriedades (separando por ".")
        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' não existe. " + "Corrija ou remova essa propriedade e tente novamente.", path);

        Problem problem = createProblemBuilder(status, problemType, detail)
                .mensagemParaUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL) // Mensagem que pode ser utilizada para um usuario final
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /**
     * Metodo que captura excessão ao digitar um tipo de valor invalido da propriedade na representação
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', " + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .mensagemParaUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL) // Mensagem que pode ser utilizada para um usuario final
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    /**
     * Metodo que retorna a propriedade que recebeu o tipo de valor invalido, e qual e o tipo certo valido para ser digitado
     * @param references
     * @return
     */
    private String joinPath(List<Reference> references) {
        return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
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
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

        Problem problem = createProblemBuilder(status, problemType, detail) // Antes do builder podemos customizar mais propriedades adicionais
                .mensagemParaUsuario(detail) // Mensagem que pode ser utilizada para um usuario final
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
                .mensagemParaUsuario(detail) // Mensagem que pode ser utilizada para um usuario final
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
                .mensagemParaUsuario(detail) // Mensagem que pode ser utilizada para um usuario final
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
                    .title(status.getReasonPhrase()) // Descreve o titulo do erro que
                    .status(status.value()) // Pega o HTTP.Status que é uma enumercao
                    .mensagemParaUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build();

        } else if(body instanceof String){ // Se o corpo(body) for uma instancia de uma String
            body = Problem.builder()
                    .title((String) body) // Faz o cast do Object(body) para String com o titulo do erro
                    .status(status.value()) // Pega o HTTP.Status que é uma enumercao
                    .mensagemParaUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL)
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
                .timestamp(LocalDateTime.now())
                .status(status.value()) // Pega o valor do status
                .type(problemType.getUri()) // Pega o valor da uri que esta dentro do enum ProblemType
                .title(problemType.getTitle()) // Pega o valor do title que esta dentro do enum ProblemType
                .detail(detail); // Passa do detalhe do problema
              //.build(); -> O Builde nao e feito pq é para ser construido nesse momento a instancia do problem
    }


    /**
     * Metodo que trata as Exceptions do tipo TypeMismatchExceptione seus subtipos, como por exemplo a MethodArgumentTypeMismatchException.
     *
     * MethodArgumentTypeMismatchException é um subtipo de TypeMismatchException
     * ResponseEntityExceptionHandler já trata TypeMismatchException de forma mais abrangente
     * Então, especializamos o método handleTypeMismatch e verificamos se a exception é uma instância de MethodArgumentTypeMismatchException
     * Se for, chamamos um método especialista em tratar esse tipo de exception
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {

        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }


    /**
     * Metodo que trata a exception especifica MethodArgumentTypeMismatchException que faz parte da exception super TypeMismatchException.
     *
     * Quando e passado um valor errado na URL, gera esse erro.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .mensagemParaUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL) // Mensagem que pode ser utilizada para um usuario final
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    /**
     * Metodo de exception que trata o erro caso seja passado uma URL invalida
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.",
                ex.getRequestURL());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .mensagemParaUsuario(MSG_ERRO_GENERICA_USUARIO_FINAL) // Mensagem que pode ser utilizada para um usuario final
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
        String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

        // Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
        // fazendo logging) para mostrar a stacktrace no console
        // Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
        // para você durante, especialmente na fase de desenvolvimento
        ex.printStackTrace();

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

}
