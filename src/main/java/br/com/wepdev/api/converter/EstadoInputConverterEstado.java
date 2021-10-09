package br.com.wepdev.api.converter;

import br.com.wepdev.api.inputDTO.EstadoInputDTO;
import br.com.wepdev.domain.model.Estado;
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
     * @param estadoInputDTO
     * @return
     */
    public Estado converteInputParaEntidade(EstadoInputDTO estadoInputDTO){
        return modelMapper.map(estadoInputDTO, Estado.class);
    }


    /**
     * Metodo que copia as propriedades de RestauranteINPUT para Restaurante, utilizado no atualizar()
     * @param estadoInputDTO
     * @param estado
     */
    public void copiaInputParaEntidade(EstadoInputDTO estadoInputDTO, Estado estado){

        modelMapper.map(estadoInputDTO, estado);

    }

















}
