package br.com.wepdev.core.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

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


    /**
     * Habilitando o filtro para etag, que permite a utilização da representação que esta armazenada no cache do navegador em forma de stale(velha - antiga) , fresh(nova, fresca)
     *
     * Metodo que ao receber uma requisição no momento da resposta ele gera um hash e coloca o cabeçalho etag, e tambem verifica se
     * o hash da resposta coincide com a etag que esta no If-None-Match(cabeçalho que vem na requisição), se for igual(se coincidir, ele retorna um 304(unmodified - não modificado))
     * se nao for igual a requisição prosegue e é adicionado o cabeçalho etag
     * @return
     */
    @Bean
    public Filter shallowEtagHeaderFilter(){
        return new ShallowEtagHeaderFilter();
    }
}
