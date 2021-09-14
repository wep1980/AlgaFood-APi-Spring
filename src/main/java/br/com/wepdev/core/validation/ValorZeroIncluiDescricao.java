package br.com.wepdev.core.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // Essa anotação so pode ser usada em uma classe ou interface
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValorZeroIncluiDescricaoValidator.class}) //Vinculando a anotacao, a classe ValorZeroIncluiDescricaoValidator, que e a classe que possui a regra de validação
public @interface ValorZeroIncluiDescricao {


    String message() default "descrição obrigatória inválida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    String valorField();

    String descricaoField();

    String descricaoObrigatoria();
}
