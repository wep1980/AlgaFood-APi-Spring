package br.com.wepdev.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * Number -> Suporta qualquer tipo de numero, Integer, BigDecimal, Double, Long.... etc
 */
public class MultiploValidator implements ConstraintValidator<Multiplo, Number> {


    // Variavel que o numero anotado na anotação, -> @Multiplo(numero = 5)
    private int numeroMultiplo;


    /**
     * Metodo que inicializa o validador para preparar pra chamadas futuras
     * @param constraintAnnotation
     */
    @Override
    public void initialize(Multiplo constraintAnnotation) {
        this.numeroMultiplo = constraintAnnotation.numero();
    }


    /**
     * Metodo que implementa a logica de validacao
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        boolean valido = true;
        if(value != null){
            var valorDecimal = BigDecimal.valueOf(value.doubleValue()); // Convertendo o Number para double, BigDecimal do numero que sera validado
            var multiploDecimal = BigDecimal.valueOf(this.numeroMultiplo); // BigDecimal do numero pra saber se é multiplo do valorDecimal
            var resto = valorDecimal.remainder(multiploDecimal); // Pega o resto da divisão dos valores, se o resto for 0, o numero e multiplo

            valido = BigDecimal.ZERO.compareTo(resto) == 0;// compareTo -> compara um BigDecimal com outro BigDecimal
        }

        return valido;
    }























}
