package br.com.wepdev.infrastructure.service.storage;


import br.com.wepdev.core.storage.StorageProperties;
import br.com.wepdev.domain.service.FotoStorageService;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class S3FotoStorageService implements FotoStorageService {


    @Autowired
    private AmazonS3 amazonS3; // registardo como um Bean spring em AmazonS3Config

    @Autowired
    private StorageProperties storageProperties;


    /**
     * Metodo que armazena(faz upload) da foto na AmazonS3
     * @param novaFoto
     */
    @Override
    public void armazenar(NovaFoto novaFoto) {

        try {
            String caminhoArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());

            var objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(novaFoto.getContentType()); // Passando o contentType para que o navegador abra pela url a foto depois de salva no amazonS3

            // Submete para api da amazon uma requisição para colocar um objeto
            var putObjectRequest = new PutObjectRequest(
                    storageProperties.getS3().getBucket(),
                    caminhoArquivo,
                    novaFoto.getInputStream(),
                    objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead); // adicionando permissão de acesso publico de leitura as fotos
            amazonS3.putObject(putObjectRequest);
        } catch (Exception e){
            throw new StorageException("Não foi possível enviar arquivo para Amazon S3.", e);

        }

    }


    private String getCaminhoArquivo(String nomeArquivo) {
        return String.format("%s/%s", storageProperties.getS3().getDiretorioFotos(), nomeArquivo);
    }


    @Override
    public void remover(String nomeArquivo) {

    }

    @Override
    public InputStream recuperar(String nomeArquivo) {
        return null;
    }
}
