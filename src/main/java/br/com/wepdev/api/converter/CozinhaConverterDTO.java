package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.CozinhaDTO;
import br.com.wepdev.api.DTO.EstadoDTO;
import br.com.wepdev.domain.model.Cozinha;
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
public class CozinhaConverterDTO {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Metodo que converte Estado em EstadoDTO
     * @param cozinha
     * @return
     */
    public CozinhaDTO toModel(Cozinha cozinha) {
        return modelMapper.map(cozinha, CozinhaDTO.class);
    }


    /**
     * Metodo que converte uma lista de Estado em uma lista de Estado
     * @param cozinhas
     * @return
     */
    public List<CozinhaDTO> toCollectionModel(List<Cozinha> cozinhas){

        // Convertendo uma lista de Restaurantes para uma lista de RestauranteDTO
        return cozinhas.stream().map(cozinha -> toModel(cozinha))
                .collect(Collectors.toList());
    }
}
