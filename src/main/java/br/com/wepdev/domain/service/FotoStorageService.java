package br.com.wepdev.domain.service;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;

/**
 * Interface de serviço de armazenagem de fotos
 */
public interface FotoStorageService {


    void armazenar(NovaFoto novaFoto);




    /**
     * Classe interna
     */
    @Getter
    @Builder // Padrão mais facil para construção de objeto
    class NovaFoto{

        private String nomeArquivo;
        private InputStream inputStream; // Fluxo de entrada(leitura) do arquivo

    }
}
