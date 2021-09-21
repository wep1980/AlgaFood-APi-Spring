package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.INPUT.RestauranteINPUT;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class RestInputConverterRestaurante {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
    */
    @Autowired
    private ModelMapper modelMapper;



    /**
     * Metodo que transforma RestauranteInput para Restaurante
     * @param restauranteInput
     * @return
     */
    public Restaurante toDomainObject(RestauranteINPUT restauranteInput){
        return modelMapper.map(restauranteInput, Restaurante.class);

    }
}
