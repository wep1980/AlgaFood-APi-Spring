package br.com.wepdev.core.modelmapper;

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
        return new ModelMapper();
    }
}
