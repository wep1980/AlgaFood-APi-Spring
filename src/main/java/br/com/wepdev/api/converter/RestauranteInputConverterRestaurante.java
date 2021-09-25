package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.INPUT.RestauranteINPUT;
import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class RestauranteInputConverterRestaurante {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
    */
    @Autowired
    private ModelMapper modelMapper;



    /**
     * Metodo que transforma RestauranteInput para Restaurante, utilizado no
     * @param restauranteInput
     * @return
     */
    public Restaurante toDomainObject(RestauranteINPUT restauranteInput){
        return modelMapper.map(restauranteInput, Restaurante.class);
    }


    /**
     * Metodo que copia as propriedades de RestauranteINPUT para Restaurante, utilizado no atualizar()
     * @param restauranteInput
     * @param restaurante
     */
    public void copyToDomainObject(RestauranteINPUT restauranteInput, Restaurante restaurante){

        /**
         * E necessario instanciar uma nova cozinha, para evitar o problema de no momento da copia, o jpa entender que exista uma tentativa de troca de id
         * no id da cozinha, ao inves de um referencia de tipo de cozinha para o Restaurante.
         * Para evitar org.hibernate.HibernateException: identifier of an instance of
         * com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
         */
        restaurante.setCozinha(new Cozinha());

        /**
         * E necessario instanciar uma novo Endereco, para evitar o problema de no momento da copia, o jpa entender que exista uma tentativa de troca de id
         * no id da cidade, ao inves de uma nova referencia em uma cidade para o Endereco.
         * Para evitar org.hibernate.HibernateException: identifier of an instance of
         * com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
         */
        if(restaurante.getEndereco() != null){
            restaurante.getEndereco().setCidade(new Cidade());
        }

        modelMapper.map(restauranteInput, restaurante);

    }

















}
