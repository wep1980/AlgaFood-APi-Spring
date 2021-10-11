package br.com.wepdev.domain.service;

import br.com.wepdev.domain.model.FotoProduto;
import br.com.wepdev.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CatalogoFotoProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;


    /**
     * Como na tabela FotoProduto o campo produto_id alem de identificador e a chave primaria, nao pode existir produto_id duplicado.
     *
     * Na implementacao ao tentar gravar uma nova foto, se ja existir uma foto cadastrada ela sera excluida e a nova foto sera salva.
     *
     * Na regra de negócio so e permitido gravar uma foto para cada produto.
     *
     * A foto sera sempre buscada pelo restauranteId e produtoId pois o mesmo produto pode existir em mais de um restaurante.
     *
     */
    @Transactional
    public FotoProduto salvar(FotoProduto foto){

        Long restauranteId = foto.getRestauranteId();
        Long produtoId = foto.getProduto().getId();

        /**
         * Antes de salvar a foto do produto e verificado se ela ja existe, se existir ela e excluida para ser salva uma nova foto
         */
        Optional<FotoProduto> fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);
        if(fotoExistente.isPresent()){
            produtoRepository.delete(fotoExistente.get());
        }
        return produtoRepository.save(foto);
    }
}
