package br.com.wepdev.core.storage;

import com.amazonaws.regions.Regions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

/**
 * Classe de configuração das propriedades customizadas por mim no application.properties
 */
@Getter
@Setter
@Component
@ConfigurationProperties("algafood.storage")
public class StorageProperties {


    private Local local = new Local();
    private S3 s3 = new S3();
    private TipoStorage tipo = TipoStorage.LOCAL; // Esse e o tipo de storage padrão


    public enum TipoStorage {
        LOCAL, S3
    }

    /**
     * Metodo que identifica qual o tipo de storage que sera usado, o LocalFotoStorageService ou S3FotoStorageService
     */





            // Classe dentro de uma classe com os campos
            @Getter
            @Setter
            public class Local {

                private Path diretorioFotos;


            }



            // Classe dentro de uma classe com os campos
            @Getter
            @Setter
            public class S3 {

                private String idChaveAcesso;
                private String chaveAcessoSecreta;
                private String bucket;
                private Regions regiao;
                private String diretorioFotos;

            }
}
