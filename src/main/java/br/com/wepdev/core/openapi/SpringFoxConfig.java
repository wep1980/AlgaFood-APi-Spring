package br.com.wepdev.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

// WebMvcConfigurer -> Interface usada para customizar o Spring MCV no projeto
@Configuration
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
                .build(); // retorna o Docket
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
}
