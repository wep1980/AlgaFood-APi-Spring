package br.com.wepdev.domain.service;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.util.UUID;

/**
 * Interface de serviço de armazenagem de fotos
 */
public interface FotoStorageService {

    // Com classe NovaFoto abaixo, evitata se ficar passando varios argumentos dentro do construtor
    void armazenar(NovaFoto novaFoto);

    /**
     * Metodo com implementação para mudança de nome do arquivo(foto), pois caso tenha upload de fotos com nomes identicos, uma foto não sera substituida por
     * outra.
     */
    default String gerarNomeArquivo(String nomeOriginal){
        return UUID.randomUUID().toString() + "_" + nomeOriginal;
    }


    /**
     * Classe interna
     */
    @Getter
    @Builder // Padrão mais facil para construção de objeto
    class NovaFoto {

        private String nomeArquivo;
        private InputStream inputStream; // Fluxo de entrada(leitura) do arquivo

    }
}
