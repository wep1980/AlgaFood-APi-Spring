package br.com.wepdev.domain.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Map;
import java.util.Set;

public interface EnvioEmailService {


    void enviar(Mensagem mensagem);


    @Getter
    @Builder
    class Mensagem{

        @Singular // Anotação do lombok, singulariza o Set, dessa forma e passado um destinatario de cada vez
        private Set<String> destinatarios;

        @NonNull // Anotacao do lombok para validar que seja passado um assunto
        private String assunto;

        @NonNull // Anotacao do lombok para validar que seja passado um corpo
        private String corpo; // Texto do corpo

        @Singular("variavel")// Anotação do lombok, singulariza o Map, dessa forma é passado uma variavel de cada vez. ("variavel") -> nome customizado para o singular
        private Map<String, Object> variaveis; //
    }
}
