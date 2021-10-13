package br.com.wepdev.infrastructure.service.storage;


import br.com.wepdev.domain.service.FotoStorageService;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class S3FotoStorageService implements FotoStorageService {


    @Autowired
    private AmazonS3 amazonS3; // registardo como um Bean spring em AmazonS3Config



    @Override
    public void armazenar(NovaFoto novaFoto) {

    }

    @Override
    public void remover(String nomeArquivo) {

    }

    @Override
    public InputStream recuperar(String nomeArquivo) {
        return null;
    }
}
