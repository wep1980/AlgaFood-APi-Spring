package br.com.wepdev.core.modelmapper;

import br.com.wepdev.api.DTO.RestauranteDTO;
import br.com.wepdev.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {


    /**
     * Criando uma instancia de ModelMapper dentro do Spring
     * @return
     */
    @Bean
    public ModelMapper modelMapper(){
        var modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Restaurante.class, RestauranteDTO.class)
                .addMapping(Restaurante::getTaxaFrete, RestauranteDTO::setPrecoFrete);
        return modelMapper;

    }
}
