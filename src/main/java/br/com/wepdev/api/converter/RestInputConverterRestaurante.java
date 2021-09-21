package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.INPUT.RestauranteINPUT;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.model.Restaurante;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class RestInputConverterRestaurante {



    /**
     * Metodo que transforma RestauranteInput para Restaurante
     * @param restauranteInput
     * @return
     */
    public Restaurante toDomainObject(RestauranteINPUT restauranteInput){
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(restauranteInput.getNome());
        restaurante.setTaxaFrete(restauranteInput.getTaxaFrete());

        Cozinha cozinha = new Cozinha();
        cozinha.setId(restauranteInput.getCozinha().getId());

        restaurante.setCozinha(cozinha);

        return restaurante;
    }
}
