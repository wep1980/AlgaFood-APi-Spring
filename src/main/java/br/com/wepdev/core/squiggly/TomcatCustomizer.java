package br.com.wepdev.core.squiggly;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * Classe que customiza o tomcat, aqui estamos permitindo que o tomcat receba na requisição os [], para que nao haja a necessidade de encoding como no exemplo da
 * requisicao abaixo.
 * {{host}}/pedidos?fields=codigo,valorTotal,sub*,cliente%5Bid,nome%5D -> COM ENCODING
 * {{host}}/pedidos?fields=codigo,valorTotal,sub*,cliente[id,nome] -> SEM ENCODING e a configuracao abaixo permite que seja feita requisicoes com [].
 *
 * O Spring tem dentro dele um tomcat
 */
@Component
public class TomcatCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {


    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> connector.setAttribute("relaxedQueryChars", "[]"));
    }
    
}