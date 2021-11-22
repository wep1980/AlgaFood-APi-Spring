package br.com.wepdev.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {


    /**
     * Metodo que disponibiliza uma instancia de Docket.
     * Docket -> Classe que representa a configuração da Api para utilizar a especificação openApi
     * @return
     */
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.OAS_30)
                .select() // Retorna um builder que ira selecionar os end points que devem ser expostos na definição do JSON
                .apis(RequestHandlerSelectors.any()) // especifica os controladores e end points que serão escaneados, RequestHandlerSelectors.any() -> tudo que tiver na api
                .build(); // retorna o Docket
    }
}
