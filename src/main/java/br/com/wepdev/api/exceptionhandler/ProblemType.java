package br.com.wepdev.api.exceptionhandler;

import lombok.Getter;

/**
 * Classe de ENUM com as constantes de tipos de problemas que podem ocorrer nas exceptions
 */
@Getter
public enum ProblemType {

                       // Recebe o titulo do problema ---- Caminho que esta concatenado com o que ja tem dentro da uri
    RECURSO_NAO_ENCONTRADO("Recurso não encontrado", "/recurso-nao-encontrado"),
    ENTIDADE_EM_USO("Entidade em uso", "/entidade-em-uso"),
    ERRO_NEGOCIO("Violação de regra de negócio", "/erro-negocio"),
    MENSAGEM_INCOMPREENSIVEL("Mensagem incompreensível" , "/mensagem-incompreensível"),
    PARAMETRO_INVALIDO("Parametro inválido" , "/parametro-invalido"),
    ERRO_DE_SISTEMA("Erro de sistema" , "/erro de sistema");



    private String title; // Titulo do tipo do problema
    private String uri;  // uri do tipo do problema


    ProblemType(String title, String path) {
        this.title = title;
        this.uri = "https://algafood.com.br" + path;
    }
}
