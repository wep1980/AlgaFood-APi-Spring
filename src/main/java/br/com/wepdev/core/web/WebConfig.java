package br.com.wepdev.core.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * implements WebMvcConfigurer -> interface do spring MVC que define metodos de callback para customização do spring mvc
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {




    /**
     * Metodo que configura e habilita o CORS globalmente
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// Permite qualquer url
                .allowedMethods("*"); // Habilita todos os metodos. Por padrão ja sao permitidos os metodos simples ("GET", "HEAD", "POST")
                //.allowedOrigins("*") // permite qualquer origin, ou poder especificar uma ou mais ("http://www.algafood.local:8000")
                //.maxAge(30); // configura o tempo do preflight
    }
}
