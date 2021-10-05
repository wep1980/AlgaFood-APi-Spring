package br.com.wepdev.core.squiggly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SquigglyConfig {


    /**
     * Metodo que produz a instancia de FilterRegistrationBean.
     * Na realidade e um filter de servlet.
     *
     * Adicionando um filtro nas requisicoes http.
     *
     * Sempre que uma requisição Http chegar na API, vai passar por esse filtro aqui, ou seja esse filtro sempre vai ter a chance de poder fazer alguma coisa
     * antes de retornar para o consumidor da API.
     *
     * ObjectMapper -> objeto do jackson que vai receber as propriedades para o filtro do Squiggly
     *
     * OBS : java.lang.IllegalArgumentException: Invalid character found in the request target. The valid characters are defined in RFC 7230 and RFC 398,
     * ESSE ERRO ACONTECE AO PASSAR CARACTER INVALIDO NA REQUISICAO, O TOMCAT NAO ACEITA [], EXEMPLO DE UMA REQUISICAO NAO ACEITA
     * {{host}}/pedidos?fields=codigo,valorTotal,sub*,cliente[id,nome]. Para funcionar e necessario fazer o encoding dos [], so buscar no google por enconding de URL.
     * Requisicao com os [] encodados = {{host}}/pedidos?fields=codigo,valorTotal,sub*,cliente%5Bid,nome%5D
     *
     * Metodo funciona para todos
     *
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<SquigglyRequestFilter> squigglyRequestFilter(ObjectMapper objectMapper){
        Squiggly.init(objectMapper, new RequestSquigglyContextProvider("campos", null));

        var urlPatterns = Arrays.asList("/pedidos/*", "/restaurantes/*"); // Urls aceitas pelo Squiggly

        var filterRegistration = new FilterRegistrationBean<SquigglyRequestFilter>();
        filterRegistration.setFilter(new SquigglyRequestFilter());
        filterRegistration.setOrder(1);
        filterRegistration.setUrlPatterns(urlPatterns);// Padroes de URL aceitas pelo Squiggly, se nao settar esse metodo por padrao ele aceita todas as URLs

        return filterRegistration;

    }
}
