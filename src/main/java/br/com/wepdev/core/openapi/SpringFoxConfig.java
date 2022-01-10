package br.com.wepdev.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

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
                .apiInfo(apiInfo()) // Passando as informações do cabeçalho da pagina HTML do swagger
                .tags(new Tag("Cidades", "Gerencia as cidades")); // Adicionando uma nova Tag na documentação
    }


//    @Bean
//    public Docket apiDocket() {
//        return new Docket(DocumentationType.OAS_30)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))
//                .paths(PathSelectors.any())
//                .build()
//                .useDefaultResponseMessages(false)
//                .globalResponses(HttpMethod.GET, globalGetResponseMessages())
//                .globalResponses(HttpMethod.POST, globalPostPutResponseMessages())
//                .globalResponses(HttpMethod.PUT, globalPostPutResponseMessages())
//                .globalResponses(HttpMethod.DELETE, globalDeleteResponseMessages())
//                .apiInfo(apiInfo())
//                .tags(new Tag("Cidades", "Gerencia as cidades"));
//    }


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
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno no servidor")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
                        .description("Recurso não possui representação que poderia ser aceita pelo consumidor")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
                        .description("Requisição recusada porque o corpo está em um formato não suportado")
                        .build()
        );
    }


    private List<Response> globalDeleteResponseMessages() {
        return Arrays.asList(
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .description("Requisição inválida (erro do cliente)")
                        .build(),
                new ResponseBuilder()
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .description("Erro interno no servidor")
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


















}
