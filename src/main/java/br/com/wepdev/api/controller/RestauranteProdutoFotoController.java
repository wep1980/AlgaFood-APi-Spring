package br.com.wepdev.api.controller;

import br.com.wepdev.api.inputDTO.ProdutoFotoInputDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {



    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)// Mepeado no contentType para receber apenas o MULTIPART_FORM_DATA_VALUE
    public void atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, ProdutoFotoInputDTO produtoFotoInputDTO) {

        /**
         * O uploads estao  sendo salvos na pasta C:/Users/Waldir/Desktop/upload
         */
        var nomeArquivo = UUID.randomUUID().toString() + "_" + produtoFotoInputDTO.getArquivo().getOriginalFilename();

        var arquivoFoto = Path.of("/Users/Waldir/Desktop/upload/catalogo", nomeArquivo);

        System.out.println(arquivoFoto);
        System.out.println(produtoFotoInputDTO.getDescricao());
        System.out.println(produtoFotoInputDTO.getArquivo().getContentType());

        try {
            produtoFotoInputDTO.getArquivo().transferTo(arquivoFoto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
