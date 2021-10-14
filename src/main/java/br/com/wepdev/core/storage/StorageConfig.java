package br.com.wepdev.core.storage;

import br.com.wepdev.domain.service.FotoStorageService;
import br.com.wepdev.infrastructure.service.storage.LocalFotoStorageService;
import br.com.wepdev.infrastructure.service.storage.S3FotoStorageService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {


    @Autowired
    private StorageProperties storageProperties;




    @Bean // Produz uma instancia de amazon S3
    //Esse bean so sera instanciado caso no application.properties a propriedade algafood.storage.tipo for igual a S3
    @ConditionalOnProperty(name = "algafood.storage.tipo", havingValue = "s3")
    public AmazonS3 amazonS3(){

        // Passando as credenciais de acesso
        var credentials = new BasicAWSCredentials(storageProperties.getS3().getIdChaveAcesso(), storageProperties.getS3().getChaveAcessoSecreta());

         return AmazonS3ClientBuilder.standard()
                 .withCredentials(new AWSStaticCredentialsProvider(credentials))
                 .withRegion(storageProperties.getS3().getRegiao())
                 .build();
    }


    /**
     * Metodo que seleciona qual Bean sera utilizado, o LocalFotoStorageService ou S3FotoStorageService
     */
    @Bean
    public FotoStorageService fotoStorageService(){
       if(StorageProperties.TipoStorage.S3.equals(storageProperties.getTipo())){
           return new S3FotoStorageService();
       } else {
           return new LocalFotoStorageService();
       }

    }
}
