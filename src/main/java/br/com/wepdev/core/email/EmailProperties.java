package br.com.wepdev.core.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;


@Validated //(Classe validada) -> Para funcionar as validações colocas nos campos abaixo
@Getter
@Setter
@Component
@ConfigurationProperties("algafood.email") // Criando uma classe com novas propriedades para serem configuradas no application.properties
public class EmailProperties {


    @NotNull // Essa propriedade não pode ser nula
    private String remetente;
}
