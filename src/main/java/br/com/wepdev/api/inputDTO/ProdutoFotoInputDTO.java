package br.com.wepdev.api.inputDTO;

import br.com.wepdev.core.validation.FileSize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProdutoFotoInputDTO {


    @NotNull
    @FileSize(max = "500KB") // Anotação customizada criada por mim para validar o tamanho maximo do arquivo para upload
    private MultipartFile arquivo;

    @NotBlank
    private String descricao;

}
