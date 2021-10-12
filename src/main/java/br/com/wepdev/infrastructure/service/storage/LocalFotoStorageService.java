package br.com.wepdev.infrastructure.service.storage;

import br.com.wepdev.core.storage.StorageProperties;
import br.com.wepdev.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Classe de serviço de armazenagem de fotos em algum dispositivo
 */
@Service // Transforma em Bean Spring
public class LocalFotoStorageService implements FotoStorageService {

    // Injetando um valor de uma propriedade do application.properties
    //@Value("${algafood.storage.local.diretorio-fotos}")
    //private Path diretorioFotos;


    @Autowired
    private StorageProperties storageProperties;

    /**
     * OBS : Esse metodo não esta utilizando o Multipart que é uma instancia ligada a web, nela tem o metodo transferTo() que torna mais facil a implementação,
     * mas da forma da implementação abaixo sem o Multipart a gente nao fica aclopado a ele, pois ele e uma classe em um pacote de WEB, e nao faz sentido ter algo de
     * web dentro de um serviço
     */
    @Override
    public void armazenar(NovaFoto novaFoto) {

        try {
        //Caminho de armazenamento, o caminho nao fica ficar fixo aqui como na forma abaixo
        //Path arquivoPath = Path.of("/Users/Waldir/Desktop/upload/catalogo", novaFoto.getNomeArquivo());
        Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

            // Copia os dados recebidos dentro de novaFoto para o caminho arquivoPath (Copiando de um lugar para outro)
            FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
        } catch (Exception e) {
            // StorageException -> exception customizada
            throw new StorageException("Não possível armazenar arquivo.", e);
        }
    }


    /**
     * Remove arquivos no disco local
     * @param nomeArquivo
     */
    @Override
    public void remover(String nomeArquivo) {

        try {
            Path arquivoPath = getArquivoPath(nomeArquivo); // Pegando o caminho do arquivo a ser removido
            Files.deleteIfExists(arquivoPath);
        } catch (IOException e) {
            // StorageException -> exception customizada
            throw new StorageException("Não possível excluir arquivo.", e);
        }

    }


    /**
     * Metodo que retorna juntando o diretorio(Path) da pasta (Local de armazenamento) e o nome do arquivo.
     * retorna o caminho completo de onde o arquivo vai ficar
     */
    private Path getArquivoPath(String nomeArquivo){
        return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));

    }


    /**
     * Metodo que recupera os arquivos do disco local para servir os consumidores da APi,
     * fornecer os dados desse arquivo para download
     */
    @Override
    public InputStream recuperar(String nomeArquivo) {
        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);

            return Files.newInputStream(arquivoPath);
        } catch (Exception e) {
            throw new StorageException("Não foi possível recuperar arquivo.", e);
        }
    }
}
