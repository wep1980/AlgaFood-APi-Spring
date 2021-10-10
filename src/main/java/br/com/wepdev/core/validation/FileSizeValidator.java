package br.com.wepdev.core.validation;

import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * A anotação FileSize valida um MultipartFile
 */
public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    /*
    DataSize -> classe do proprio Spring que representa um tamanho de dado
     */
    private DataSize maxSize;


    /**
     * Metodo que inicializa o validador para preparar pra chamadas futuras
     * @param constraintAnnotation
     */
    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.maxSize = DataSize.parse(constraintAnnotation.max()); // Fazendo o parse da String para um Datasize

    }


    /**
     * Se o multipart for nulo ou se o multipart for menor ou igual ao maxSize ta valido, entao pega se o bytes do maxSize e compara com o multipart
     * enviado na requisição. Se for maior ta invalido,
     *
     */
    @Override
    public boolean isValid(MultipartFile multipart, ConstraintValidatorContext context) {
        return multipart == null || multipart.getSize() <= this.maxSize.toBytes();

    }

}
