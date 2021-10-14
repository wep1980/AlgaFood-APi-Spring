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


    // Metodo que substitui ou grava uma foto de um produto, se a foto do produto for a mesma e feito a substituição do arquivo no disco local
    default void substituir(String nomeArquivoAntigo, NovaFoto novaFoto){
        this.armazenar(novaFoto);

        if(nomeArquivoAntigo != null){
           this.remover(nomeArquivoAntigo);
        }
    }

    /**
     * Metodo que recupera os arquivos do disco local para servir os consumidores da APi,
     * fornecer os dados desse arquivo para download
     */
    void remover(String nomeArquivo);


    FotoRecuperada recuperar(String nomeArquivo);



    /**
     * Classe interna
     */
    @Getter
    @Builder // Padrão mais facil para construção de objeto
    class NovaFoto {

        private String nomeArquivo;
        private InputStream inputStream; // Fluxo de entrada(leitura) do arquivo
        private String contentType; // Propriedade criada para armazenamento no amazonS3, para armazenar no disco local, não é utilizada

    }


    /**
     * Classe que recupera as fotos armazenadas no AmazonS3
     */
    @Builder
    @Getter
    class FotoRecuperada{

        private InputStream inputStream;
        private String url;


        public boolean temUrl(){
            return url != null;
        }

        public boolean temInputStream(){
            return inputStream != null;
        }

    }


}
