package br.com.wepdev.core.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {


    @Autowired
    private StorageProperties storageProperties;




    @Bean // Produz uma instancia de amazon S3
    public AmazonS3 amazonS3(){

        // Passando as credenciais de acesso
        var credentials = new BasicAWSCredentials(storageProperties.getS3().getIdChaveAcesso(), storageProperties.getS3().getChaveAcessoSecreta());

         return AmazonS3ClientBuilder.standard()
                 .withCredentials(new AWSStaticCredentialsProvider(credentials))
                 .withRegion(storageProperties.getS3().getRegiao())
                 .build();
    }
}
