package br.com.wepdev.api.DTOentrada;

import br.com.wepdev.core.validation.FileContentType;
import br.com.wepdev.core.validation.FileSize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProdutoFotoInputDTO {


    @NotNull
    @FileSize(max = "500KB") // Anotação customizada criada por mim para validar o tamanho maximo do arquivo para upload
    @FileContentType(allowed = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE }) // Anotação customizada criada por mim para validar os tipos de media suportada
    private MultipartFile arquivo;

    @NotBlank
    private String descricao;

}
