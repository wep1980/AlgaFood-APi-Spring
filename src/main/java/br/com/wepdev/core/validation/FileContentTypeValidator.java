package br.com.wepdev.core.validation;

import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * A anotação FileSize valida um MultipartFile
 */
public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {


    private List<String> allowedContentTypes;


    /**
     * Metodo que inicializa o validador para preparar pra chamadas futuras
     */
    @Override
    public void initialize(FileContentType constraint) {
        this.allowedContentTypes = Arrays.asList(constraint.allowed());
    }



    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        return multipartFile == null || this.allowedContentTypes.contains(multipartFile.getContentType());
    }

}
