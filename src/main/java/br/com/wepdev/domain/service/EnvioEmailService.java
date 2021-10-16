package br.com.wepdev.domain.service;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Set;

public interface EnvioEmailService {


    void enviar(Mensagem mensagem);


    @Getter
    @Builder
    class Mensagem{

        @Singular // Anotação do lombok, singulariza o Set
        private Set<String> destinatarios;

        @NonNull // Anotacao do lombok para validar que seja passado um assunto
        private String assunto;

        @NonNull // Anotacao do lombok para validar que seja passado um corpo
        private String corpo;
    }
}
