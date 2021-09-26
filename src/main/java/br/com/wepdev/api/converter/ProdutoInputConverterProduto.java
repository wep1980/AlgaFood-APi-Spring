package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.INPUT.GrupoInputDTO;
import br.com.wepdev.api.DTO.INPUT.ProdutoInputDTO;
import br.com.wepdev.domain.model.Grupo;
import br.com.wepdev.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class ProdutoInputConverterProduto {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
    */
    @Autowired
    private ModelMapper modelMapper;



    /**
     * Metodo que transforma RestauranteInput para Restaurante, utilizado no
     * @param produtoInput
     * @return
     */
    public Produto converteInputParaEntidade(ProdutoInputDTO produtoInput){
        return modelMapper.map(produtoInput, Produto.class);
    }


    /**
     * Metodo que copia as propriedades de RestauranteINPUT para Restaurante, utilizado no atualizar()
     * @param produtoInput
     * @param produto
     */
    public void copiaInputParaEntidade(ProdutoInputDTO produtoInput, Produto produto){

        modelMapper.map(produtoInput, produto);

    }

















}
