package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.RestauranteDTO;
import br.com.wepdev.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que contem os metodos que convertem de Entidades para DTO
 */
@Component
public class RestauranteConverterDTO {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Metodo que converte Restaurante em RestauranteDTO
     * @param restaurante
     * @return
     */
    public RestauranteDTO converteEntidadeParaDto(Restaurante restaurante) {
        return modelMapper.map(restaurante, RestauranteDTO.class);
    }


    /**
     * Metodo que converte uma lista de Restaurante em uma lista de RestauranteDTO
     * @param restaurantes
     * @return
     */
    public List<RestauranteDTO> converteListaEntidadeParaListaDto(List<Restaurante> restaurantes){

        // Convertendo uma lista de Restaurantes para uma lista de RestauranteDTO
        return restaurantes.stream().map(restaurante -> converteEntidadeParaDto(restaurante))
                .collect(Collectors.toList());
    }
}
