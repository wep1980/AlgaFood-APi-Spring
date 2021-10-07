package br.com.wepdev.api.exceptionhandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.wepdev.core.validation.ValidacaoException;
import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.exception.NegocioException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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


    @Autowired
    private MessageSource messageSource; // Instancia injetada, é uma interface que resolve mensagens



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

        //rootCause = getRootException(ex);

        if(rootCause instanceof InvalidFormatException){
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, status,request);
        } else if(rootCause instanceof PropertyBindingException){ // PropertyBindingException trata IgnoredPropertyException e tambem UnrecognizedPropertyException
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        } else if (rootCause instanceof NegocioException){

        }


        /**
         * Enumeracao onde fica as constantes dos tipos de problemas gerados pelas exceptions, novos problemas devem ser colocados dentro dela
         * Nela possue descrições do titulo e da uri, que é um tipo de URL
         */
        //todo O ExceptionUtils.getRootCause(ex) nao esta pegando as exceptions devidas de erros
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "O corpo da requisição esta inválido. Verifique erro de sintaxe."; // Pega a informacao do detalhe da mensagem

        Problem problem = createProblemBuilder(status, problemType, detail) // Antes do builder podemos customizar mais propriedades adicionais
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL) // Mensagem que pode ser utilizada para um usuario final
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
    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        // Criei o método joinPath para reaproveitar em todos os métodos que precisam
        // concatenar os nomes das propriedades (separando por ".")
        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' não existe. " + "Corrija ou remova essa propriedade e tente novamente.", path);

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL) // Mensagem que pode ser utilizada para um usuario final
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
    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', " + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL) // Mensagem que pode ser utilizada para um usuario final
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
    public ResponseEntity<?> handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex , WebRequest resquest){

        HttpStatus status = HttpStatus.NOT_FOUND; //Entidade nao encontrada retorna sempre um NOT_FOUND 404
        String detail = ex.getMessage(); // Pega a informacao do detalhe da mensagem

        /**
         * Enumeracao onde fica as constantes dos tipos de problemas gerados pelas exceptions, novos problemas devem ser colocados dentro dela
         * Nela possue descrições do titulo e da uri, que é um tipo de URL
         */
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

        Problem problem = createProblemBuilder(status, problemType, detail) // Antes do builder podemos customizar mais propriedades adicionais
                .userMessage(detail) // Mensagem que pode ser utilizada para um usuario final
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
    public ResponseEntity<?> handleNegocio(NegocioException ex , WebRequest resquest){

        HttpStatus status = HttpStatus.BAD_REQUEST; //Entidade nao encontrada retorna sempre um BAD_REQUEST 400
        String detail = ex.getMessage(); // Pega a informacao do detalhe da mensagem

        /**
         * Enumeracao onde fica as constantes dos tipos de problemas gerados pelas exceptions, novos problemas devem ser colocados dentro dela
         * Nela possue descrições do titulo e da uri, que é um tipo de URL
         */
        ProblemType problemType = ProblemType.ERRO_NEGOCIO;

        Problem problem = createProblemBuilder(status, problemType, detail)  // Antes do builder podemos customizar mais propriedades adicionais
                .userMessage(detail) // Mensagem que pode ser utilizada para um usuario final
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
    public ResponseEntity<?> handleEntidadeEmUso(EntidadeEmUsoException ex , WebRequest resquest){

        HttpStatus status = HttpStatus.CONFLICT; //Entidade nao encontrada retorna sempre um CONFLICT 409
        String detail = ex.getMessage(); // Pega a informacao do detalhe da mensagem

        /**
         * Enumeracao onde fica as constantes dos tipos de problemas gerados pelas exceptions, novos problemas devem ser colocados dentro dela
         * Nela possue descrições do titulo e da uri, que é um tipo de URL
         */
        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;

        Problem problem = createProblemBuilder(status, problemType, detail)  // Antes do builder podemos customizar mais propriedades adicionais
                .userMessage(detail) // Mensagem que pode ser utilizada para um usuario final
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
                    .timestamp(OffsetDateTime.now())
                    .status(status.value()) // Pega o HTTP.Status que é uma enumercao
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build();

        } else if(body instanceof String){ // Se o corpo(body) for uma instancia de uma String
            body = Problem.builder()
                    .title((String) body) // Faz o cast do Object(body) para String com o titulo do erro
                    .timestamp(OffsetDateTime.now())
                    .status(status.value()) // Pega o HTTP.Status que é uma enumercao
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
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
                .timestamp(OffsetDateTime.now())
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
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL) // Mensagem que pode ser utilizada para um usuario final
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
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL) // Mensagem que pode ser utilizada para um usuario final
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

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();


        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }


    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
    }


    private ResponseEntity<Object> handleValidationInternal(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request, BindingResult bindingResult) {

        ProblemType problemType = ProblemType.DADOS_INVALIDOS;

        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        /**
         * bindingResult -> Armazena as violações de constraints.
         * Transformando a lista de erros que e do tipo BindingResult em uma lista de Problem.Campo
         * Problem.Campo.builder() -> chamando o builder do Campo, pegando o resultando dos campos, fazendo o build e trasnformando em uma lista de Problem.Campo
         *
         * Foi criado um bloco de codigo dentro da stream(), o bloco começa em fieldError -> {   e termina depois do build() }, esse bloco foi criado
         * por conta do String message
         */
        List<Problem.Objeto> problemObjetos = bindingResult.getAllErrors().stream().map(objectError -> {

                    /**
                     * Pegando a mensagem do erro com fieldError e passando a região local para as mesagens serem enviadas em portugues.
                     * Foi necessario configurar o UTF-8 no settings -> file encodings
                     */
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                     /*
                     Pegando o nome da classe na qual ocorreu o erro de validação, no exemplo o erro ocorre quando e usada a validação de classe customizada,
                     @ValorZeroIncluiDescricao que de acordo com a regra se o frete e igual a zero, no campo nome deve contem a frase frete gratis
                     */
                    String name = objectError.getObjectName();

                    if(objectError instanceof FieldError){
                        name = ((FieldError) objectError).getField();
                    }
                    return Problem.Objeto.builder()
                            .name(name)// Pegando o nome da propriedade
                            .userMessage(message) // Informando a mensagem para cada tipo de violação
                            .build();
                })
                .collect(Collectors.toList());// Transformando a stream() na lista de Problem.Campo, com as propriedade preenchidas

        Problem problem = createProblemBuilder(status, problemType, detail)
                .objetos(problemObjetos)
                .userMessage(detail)
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    /**
     * Metodo auxiliar para tratamento de exception , 500 Internar server error
     * @param ex
     * @param bindingResult
     * @param headers
     * @param status
     * @param request
     * @return
     */
    private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
                                                            HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        List<Problem.Objeto> problemObjects = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    String name = objectError.getObjectName();

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return Problem.Objeto.builder()
                            .name(name)
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .objetos(problemObjects)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    @ExceptionHandler({ ValidacaoException.class })
    public ResponseEntity<Object> handleValidacaoException(ValidacaoException ex, WebRequest request) {

        // utilizando o metodo auxiliar que poderia ser usado tambem no metodo handleMethodArgumentNotValid()
        return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
