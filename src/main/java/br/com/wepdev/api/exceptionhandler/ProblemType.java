package br.com.wepdev.api.exceptionhandler;

import lombok.Getter;

/**
 * Classe de ENUM com as constantes de tipos de problemas que podem ocorrer nas exceptions
 */
@Getter
public enum ProblemType {

                       // Recebe o titulo do problema ---- Caminho que esta concatenado com o que ja tem dentro da uri
    ENTIDADE_NAO_ENCONTRADA("Entidade não encontrada", "/entidade-nao-encontrada"),
    ENTIDADE_EM_USO("Entidade em uso", "/entidade-em-uso"),
    ERRO_NEGOCIO("Violação de regra de negócio", "/erro-negocio");



    private String title; // Titulo do tipo do problema
    private String uri;  // uri do tipo do problema


    ProblemType(String title, String path) {
        this.title = title;
        this.uri = "https://algafood.com.br" + path;
    }
}
