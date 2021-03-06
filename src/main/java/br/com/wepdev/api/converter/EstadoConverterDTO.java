package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.EstadoDTO;
import br.com.wepdev.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que contem os metodos que convertem de Entidades para DTO
 */
@Component
public class EstadoConverterDTO {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Metodo que converte Estado em EstadoDTO
     * @param estado
     * @return
     */
    public EstadoDTO converteEntidadeParaDto(Estado estado) {
        return modelMapper.map(estado, EstadoDTO.class);
    }


    /**
     * Metodo que converte uma lista de Estado em uma lista de Estado
     * @param estados
     * @return
     */
    public List<EstadoDTO> converteListaEntidadeParaListaDto(List<Estado> estados){

        // Convertendo uma lista de Restaurantes para uma lista de RestauranteDTO
        return estados.stream().map(estado -> converteEntidadeParaDto(estado))
                .collect(Collectors.toList());
    }
}
