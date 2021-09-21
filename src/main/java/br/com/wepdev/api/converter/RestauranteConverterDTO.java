package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.CozinhaDTO;
import br.com.wepdev.api.DTO.RestauranteDTO;
import br.com.wepdev.domain.model.Restaurante;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que contem os metodos que convertem de Entidades para DTO
 */
@Component
public class RestauranteConverterDTO {


    /**
     * Metodo que converte Restaurante em RestauranteDTO
     * @param restaurante
     * @return
     */
    public RestauranteDTO toModel(Restaurante restaurante) {
        CozinhaDTO cozinhaDTO = new CozinhaDTO();
        cozinhaDTO.setId(restaurante.getCozinha().getId());
        cozinhaDTO.setNome(restaurante.getCozinha().getNome());

        RestauranteDTO restauranteDTO = new RestauranteDTO();
        restauranteDTO.setId(restaurante.getId());
        restauranteDTO.setNome(restaurante.getNome());
        restauranteDTO.setTaxaFrete(restaurante.getTaxaFrete());
        restauranteDTO.setCozinha(cozinhaDTO);
        return restauranteDTO;
    }


    /**
     * Metodo que converte uma lista de Restaurante em uma lista de RestauranteDTO
     * @param restaurantes
     * @return
     */
    public List<RestauranteDTO> toCollectionModel(List<Restaurante> restaurantes){

        // Convertendo uma lista de Restaurantes para uma lista de RestauranteDTO
        return restaurantes.stream().map(restaurante -> toModel(restaurante))
                .collect(Collectors.toList());
    }
}
