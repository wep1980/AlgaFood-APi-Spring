package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTOentrada.CozinhaInputDTO;
import br.com.wepdev.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class CozinhaInputConverterCozinha {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
    */
    @Autowired
    private ModelMapper modelMapper;



    /**
     * Metodo que transforma RestauranteInput para Restaurante, utilizado no
     * @param cozinhaInput
     * @return
     */
    public Cozinha converteInputParaEntidade(CozinhaInputDTO cozinhaInput){
        return modelMapper.map(cozinhaInput, Cozinha.class);
    }


    /**
     * Metodo que copia as propriedades de RestauranteINPUT para Restaurante, utilizado no atualizar()
     * @param cozinhaInput
     * @param cozinha
     */
    public void copiaInputParaEntidade(CozinhaInputDTO cozinhaInput, Cozinha cozinha){

        modelMapper.map(cozinhaInput, cozinha);

    }

















}
