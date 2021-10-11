package br.com.wepdev.domain.service;

import br.com.wepdev.domain.model.FotoProduto;
import br.com.wepdev.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.wepdev.domain.service.FotoStorageService.NovaFoto;

import java.io.InputStream;
import java.util.Optional;


/**
 * Vai poder ser selecionado o tipo de implementação na momento de armazenar a foto(arquivo), nesse momento so tem a opção de ser salvo no disco local
 * Dessa for esta sendo diminuido o aclopamento e aumentando a coesão, pois o CatalogoFotoProdutoService não possui nenhuma informação de onde a foto
 * esta sendo armazenada, somente fotoStorageService possui a implementacao e o local onde a foto e armazenada.
 *
 * Existem os codigos linguiça, dentro deles esta toda a implementação, regra de negócio, armazenamento de arquivo e etc..., nao tem uma responsabilidade definida,
 * sao classes com uma coesão muito baixa
 */
@Service
public class CatalogoFotoProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FotoStorageService fotoStorageService; // Classe que possui a implementação e o local onde a foto e armazenada


    /**
     * Como na tabela FotoProduto o campo produto_id alem de identificador e a chave primaria, nao pode existir produto_id duplicado.
     *
     * Na implementacao ao tentar gravar uma nova foto, se ja existir uma foto cadastrada ela sera excluida e a nova foto sera salva.
     *
     * Na regra de negócio so e permitido gravar uma foto para cada produto.
     *
     * A foto sera sempre buscada pelo restauranteId e produtoId pois o mesmo produto pode existir em mais de um restaurante.
     *
     * Da forma que esta a implementação ao salvar uma foto com o mesmo nome, e gerado um UUID novo, no banco de dados o arquivo e substituido, mas
     * no local storage nao
     *
     */
    @Transactional
    public FotoProduto salvar(FotoProduto foto, InputStream dadosArquivo){

        Long restauranteId = foto.getRestauranteId();
        Long produtoId = foto.getProduto().getId();
        String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
        String nomeArquivoExistente = null; // variavel criada para substituiçao de um arquivo antigo por um novo caso haja no disco local

        /**
         * Antes de salvar a foto do produto e verificado se ela ja existe, se existir ela e excluida para ser salva uma nova foto
         */
        Optional<FotoProduto> fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);
        if(fotoExistente.isPresent()){
            nomeArquivoExistente = fotoExistente.get().getNomeArquivo(); // Pega o arquivo antigo, caso exista sera substituido
            produtoRepository.delete(fotoExistente.get());
        }

        foto.setNomeArquivo(nomeNovoArquivo);
        foto = produtoRepository.save(foto); // A foto primeiro deve ser salva no banco de dados, pq caso ocorra alguma exception a foto nao sera salva no local Storage
        produtoRepository.flush(); // descarrega(executa, todos os inserts) toda a fila no banco de dados antes de executar o codigo abaixo

        // Import de NovaFoto feito na mão  -> import br.com.wepdev.domain.service.FotoStorageService.NovaFoto;
        // Instanciando(Criando) um objeto NovaFoto utilizando o builder() do lombok
        NovaFoto novaFoto = NovaFoto.builder()
                        .nomeArquivo(foto.getNomeArquivo())
                        .inputStream(dadosArquivo)
                        .build();

        /**
         *  Se acontecer algum problema na hora de substituir ou armazenar a foto, vai acontecer um rollback, incluisive no banco de dados
         *  A foto so sera substituida se forma a mesma que estiver sendo salva novamente e for o mesmo produto
         */
        fotoStorageService.substituir(nomeArquivoExistente, novaFoto);

        /*
        Caso ocorra alguma exception no momento do save a foto vai ter sido armazenada com a chamada do metodo acima, para evitar que isso aconteça o save deve ser
        feito antes de a foto ser armazenada no LocalStorage
         */
        //return produtoRepository.save(foto);
        return foto;
    }





























}
