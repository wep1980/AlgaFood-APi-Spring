package br.com.wepdev.domain.service;

import br.com.wepdev.domain.model.FotoProduto;
import br.com.wepdev.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogoFotoProdutoService {


    @Autowired
    private ProdutoRepository produtoRepository;


    @Transactional
    public FotoProduto salvar(FotoProduto foto){
        return produtoRepository.save(foto);
    }
}
