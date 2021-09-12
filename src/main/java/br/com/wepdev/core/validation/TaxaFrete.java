package br.com.wepdev.core.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.PositiveOrZero;

/**
 * Classe de anotacao customizada para taxa frete
 * @interface -> especifica que essa classe e uma anotação
 * @Target -> Essa anotação pode ser usada em : Metodo, propriedade(atributo), tipo de anotação, construtor .... etc
 * @Retention(RetentionPolicy.RUNTIME) -> A anotação pode ser lida em tempo de execução
 * @Constraint(validatedBy = {}) -> Classe que implementa uma validação
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@PositiveOrZero
public @interface TaxaFrete {


    /**
     * Sobrescrevendo a message padrão do PositiveOrZero para a TaxaFrete.invalida que e customizada no messages.properties
     * @return
     */
    @OverridesAttribute(constraint = PositiveOrZero.class, name = "message")
    String message() default "{TaxaFrete.invalida}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
