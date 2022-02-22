package br.com.wepdev.core.openapi;

import br.com.wepdev.api.DTO.CozinhaDTO;
import br.com.wepdev.api.exceptionhandler.Problem;
import br.com.wepdev.api.openapi.model.CozinhasModelOpenApi;
import br.com.wepdev.api.openapi.model.PageableModelOpenApi;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

// versão Spring Fox 3 e Open API 3

// WebMvcConfigurer -> Interface usada para customizar o Spring MCV no projeto
@Configuration
@Import(BeanValidatorPluginsConfiguration.class) // Importando a classe de configuração do bean Validators para dentro do springFox
public class SpringFoxConfig implements WebMvcConfigurer {


    /**
     * Metodo que disponibiliza uma instancia de Docket.
     * Docket -> Classe que representa a configuração da Api para utilizar a especificação openApi
     *
     * URL para acessar a pagina HTML do Swagger --- http://localhost:8080/swagger-ui/index.html
     */
    @Bean
    public Docket apiDocket() {

        var typeResolver = new TypeResolver(); // Intancia criada e utilizada para adicionar o modelo de Problem que descreve os erros na representação

        return new Docket(DocumentationType.OAS_30)
                .select() // Retorna um builder que ira selecionar os end points que devem ser expostos na definição do JSON

                // especifica os controladores e end points que serão escaneados, RequestHandlerSelectors.any() -> todos os controladores da api
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("br.com.wepdev.api")) // escaneia apenas os controladores desse pacote
                .paths(PathSelectors.any()) // seleciona os caminhos dos end points que terao documentação, any() -> todos os caminhos
                //.paths(PathSelectors.ant("/restaurantes/*"))
                .build() // retorna o Docket
                .useDefaultResponseMessages(false) // Desabilita os codigos de status 400
                .globalResponses(HttpMethod.GET, globalGetResponseMessages()) // Configurando os codigos de mensagens padrao para o metodo GET, POST, PUT, DELETE de forma global
                .globalResponses(HttpMethod.POST, globalPostPutResponseMessages())
                .globalResponses(HttpMethod.PUT, globalPostPutResponseMessages())
                .globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
                .additionalModels(typeResolver.resolve(Problem.class)) // Lista na pagina de documentação http://api.algafood.local:8080/swagger-ui/index.html em Schemas a classe Problem.class
                .ignoredParameterTypes(ServletWebRequest.class) // Ignorando o parametro do tipo ServletWebRequest, para nao aparecer os campos na documentacao, ja que quem adiciona esse campo e o proprio spring no controller do FormaPagamento
                .directModelSubstitute(Pageable.class, PageableModelOpenApi.class) // Substituindo a classe principal pela classe customizada que foi feita para fins de documentação
                .alternateTypeRules(AlternateTypeRules.newRule(
                        typeResolver.resolve(Page.class, CozinhaDTO.class), CozinhasModelOpenApi.class)) // Criando uma regra de substituição de classe, pois o Page recebe um parametro generico, tipado como CozinhaDTO e alterna para CozinhasModelOpenApi, que e a classe para documentação
                .apiInfo(apiInfo()) // Passando as informações do cabeçalho da pagina HTML do swagger
                .tags(new Tag("Cidades", "Gerencia as cidades"), // Adicionando uma nova Tag na documentação
                        new Tag("Grupos", "Gerencia os grupos de usuários"),
                        new Tag("Cozinhas", "Gerencia as cozinhas"), // Adicionando uma nova Tag na documentação
                        new Tag("Formas de pagamento", "Gerencia as formas de pagamento"));
    }


    /**
     * Bean para fazer com que o SpringFox carregue o módulo de conversão de datas e resolva o problema na documentação de serialização
     * @return
     */
    @Bean
    public JacksonModuleRegistrar springFoxJacksonConfig() {
        return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
    }



    /**
     * versão 3 do SpringFox
     *
     * Metodo que cria uma lista de ResponseMessage para todos os metodos GET de forma global, metodo utilizado no metodo acima.
     * Colocado no metodo apenas os codigos de erro.
     * @return
     */
    private List<Response> globalGetResponseMessages(){
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno no servidor")
                        .representation( MediaType.APPLICATION_JSON ) // Adicionando um modelo de representação no corpo da resposta deste erro
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
                        .description("Recurso não possui representação que poderia ser aceita pelo consumidor")
                        .build()

//                new ResponseBuilder() // Nao funciona a descricao customizada de forma global no status 200, para funcionar e necessario customizar no proprio endpoint
//                        .code(String.valueOf(HttpStatus.OK.value()))
//                        .description("Consulta realizada com sucesso")
//                        .build()
        );
    }


    private List<Response> globalPostPutResponseMessages() {
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description("Requisição inválida (erro do cliente)")
                        .representation( MediaType.APPLICATION_JSON )// Adicionando um modelo de representação no corpo da resposta deste erro
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno no servidor")
                        .representation( MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
                        .description("Recurso não possui representação que poderia ser aceita pelo consumidor")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
                        .description("Requisição recusada porque o corpo está em um formato não suportado")
                        .representation( MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference())
                        .build()
        );
    }


    private List<Response> globalDeleteResponseMessages() {
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description("Requisição inválida (erro do cliente)")
                        .representation( MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference())
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno no servidor")
                        .representation( MediaType.APPLICATION_JSON )
                        .apply(getProblemaModelReference())
                        .build()
        );
    }


    /**
     * Metodo que faz o mapeamento de caminhos para servir arquivos estaticos do springFox swagger ui
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("swagger-ui.html") // Padrão de caminho recebido
        registry.addResourceHandler("index.html") // Padrão de caminho recebido
                .addResourceLocations("classpath:/META-INF/resources/"); // Arquivo que vai ser fornecido(servido)

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    /**
     * Metodo que modifica as informações no cabeçalho da documentação do swagger na pagina HTML
     * @return
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("AlgaFood API")
                .description("API aberta para clientes e restaurantes")
                .version("1")
                .contact(new Contact("Waldir escouto pereira", "www.linkedin.com/in/wepdev", "wepcienciadacomputacao@gmail.com"))
                .build();

    }


    /**
     * Metodo que referencia a classe Problem
     * @return
     */
    private Consumer<RepresentationBuilder> getProblemaModelReference() {
        return r -> r.model(m -> m.name("Problema")
                .referenceModel(ref -> ref.key(k -> k.qualifiedModelName(
                        q -> q.name("Problema").namespace("br.com.wepdev.api.exceptionhandler")))));
    }














}
