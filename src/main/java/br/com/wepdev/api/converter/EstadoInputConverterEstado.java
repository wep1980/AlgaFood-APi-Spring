package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.EstadoDTO;
import br.com.wepdev.api.DTO.INPUT.EstadoINPUT;
import br.com.wepdev.api.DTO.INPUT.EstadoIdINPUT;
import br.com.wepdev.api.DTO.INPUT.RestauranteINPUT;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.model.Estado;
import br.com.wepdev.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class EstadoInputConverterEstado {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
    */
    @Autowired
    private ModelMapper modelMapper;



    /**
     * Metodo que transforma RestauranteInput para Restaurante, utilizado no
     * @param estadoInput
     * @return
     */
    public Estado toDomainObject(EstadoINPUT estadoInput){
        return modelMapper.map(estadoInput, Estado.class);
    }


    /**
     * Metodo que copia as propriedades de RestauranteINPUT para Restaurante, utilizado no atualizar()
     * @param estadoInput
     * @param estado
     */
    public void copyToDomainObject(EstadoINPUT estadoInput, Estado estado){

        modelMapper.map(estadoInput, estado);

    }

















}
