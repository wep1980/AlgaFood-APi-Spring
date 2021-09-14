package br.com.wepdev.core.validation;

import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

/**
 * A Validação e por um Object, pode ser qualquer objeto
 */
public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {


    private String valorField;
    private String descricaoField;
    private String descricaoObrigatoria;


    /**
     * Metodo que inicializa o validador para preparar pra chamadas futuras
     * @param constraintAnnotation
     */
    @Override
    public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
        this.valorField = constraintAnnotation.valorField();
        this.descricaoField = constraintAnnotation.descricaoField();
        this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
    }


    /**
     * Metodo que implementa a logica de validacao. Torna Obrigatorio um restaurante que possui taxaFrete igual a 0, a colocar no nome frete gratis
     *
     * BeanUtils -> Instancia(Classe utilitaria do Spring) que ajuda o acesso aos metodos das classes que utilizam essa anotacao.
     * getPropertyDescriptor(objetoValidacao.getClass(), valorField) -> Pegando a descrição da propriedade, que no exemplo esta pegando a taxafrete que esta setada na
     * anotation que esta na classe Restaurante.
     *
     * getReadMethod() -> Busca referencia ao metodo get() da propriedade, que no exemplo e o getTaxaFrete()
     * @param objetoValidacao
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {
        boolean valido = true;

        try {
            BigDecimal valor = (BigDecimal) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), valorField).getReadMethod().invoke(objetoValidacao);
            String descricao = (String) BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), descricaoField).getReadMethod().invoke(objetoValidacao);

            // Se a variavel valido for false, quer dizer que o freteGratis é 0 e a descrição nao contem a descricaoObrigatoria
            if(valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null){

               // Pega a descricao colocando toda para minusculo, e verifica se o nome na classe Restaurante que a classe de exemplo, contem a descricaoObrigatoria,
                // que no exemplo e a descrição de freteGratis
               valido = descricao.toLowerCase().contains(this.descricaoObrigatoria.toLowerCase());
            }
            return valido;

        } catch (Exception e) {
            throw new ValidationException(e);
        }
    }
}
