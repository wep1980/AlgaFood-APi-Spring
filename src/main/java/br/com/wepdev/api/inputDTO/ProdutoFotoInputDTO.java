package br.com.wepdev.api.inputDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProdutoFotoInputDTO {


    private MultipartFile arquivo;
    private String descricao;

}
