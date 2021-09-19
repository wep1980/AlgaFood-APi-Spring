package br.com.wepdev.core.jackson;

import br.com.wepdev.api.mixin.RestauranteMixin;
import br.com.wepdev.domain.model.Restaurante;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

/**
 * Classe que faz a ligação entre as classes Restaurante e RestauranteMixin que possui as propriedades de Restaurante que possuem anotações jackson
 */
@Component
public class JacksonMixinModule extends SimpleModule {
    private static final long serialVersionUID = -5566500204080391704L;

    /**
     * Construtor, faz a ligação entre a Classe Restaurante e a RestauranteMixin
     */
    public JacksonMixinModule(){
        setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
    }
}
