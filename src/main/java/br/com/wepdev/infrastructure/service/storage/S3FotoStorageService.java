package br.com.wepdev.infrastructure.service.storage;


import br.com.wepdev.core.storage.StorageProperties;
import br.com.wepdev.domain.service.FotoStorageService;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

//@Service // Ao comentar o @Service o LocalFotoStorageService que vai ser utilizado
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

            //objectMetadata.setContentDisposition(String.format("attachment; filename=\"%s\"", novaFoto.getNomeArquivo()));

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


    @Override
    public void remover(String nomeArquivo) {
        try {
            String caminhoArquivo = getCaminhoArquivo(nomeArquivo);

            var deleteObjectRequest = new DeleteObjectRequest(
                    storageProperties.getS3().getBucket(), caminhoArquivo);

            amazonS3.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new StorageException("Não foi possível excluir arquivo na Amazon S3.", e);
        }
    }


    @Override
    public FotoRecuperada recuperar(String nomeArquivo) {
        String caminhoArquivo = getCaminhoArquivo(nomeArquivo);

        // Monta a url(pega a foto do bucket da amazonS3) sem fazer requisicao
        URL url = amazonS3.getUrl(storageProperties.getS3().getBucket(), caminhoArquivo);

        return FotoRecuperada.builder()
                .url(url.toString())
                .build(); // Retornando a foto recuperada
    }


    private String getCaminhoArquivo(String nomeArquivo) {
        return String.format("%s/%s", storageProperties.getS3().getDiretorioFotos(), nomeArquivo);
    }
}
