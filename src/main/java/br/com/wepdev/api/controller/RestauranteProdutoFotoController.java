package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.FotoProdutoDTO;
import br.com.wepdev.api.converter.FotoProdutoConverterDTO;
import br.com.wepdev.api.inputDTO.ProdutoFotoInputDTO;
import br.com.wepdev.domain.model.FotoProduto;
import br.com.wepdev.domain.model.Produto;
import br.com.wepdev.domain.service.CatalogoFotoProdutoService;
import br.com.wepdev.domain.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {



    @Autowired
    private CatalogoFotoProdutoService catalogoFotoProdutoService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private FotoProdutoConverterDTO fotoProdutoConverterDTO;


    /**
     * HttpMediaTypeNotSupportedException: Content type '' not supported --> No caso desse erro e necessario passar um contentType no postman assim :
     * multipart/form-data; boundary=0 -> colocar campo no headers do postman
     *
     * Codigo para exercitar o upload de arquivo
     *
     * @param restauranteId
     * @param produtoId
     * @param produtoFotoInputDTO
     */
//    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)// Mepeado no contentType para receber apenas o MULTIPART_FORM_DATA_VALUE
//    public void atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid ProdutoFotoInputDTO produtoFotoInputDTO) {
//
//        /**
//         * O uploads estao  sendo salvos na pasta C:/Users/Waldir/Desktop/upload
//         */
//        var nomeArquivo = UUID.randomUUID().toString() + "_" + produtoFotoInputDTO.getArquivo().getOriginalFilename();
//
//        var arquivoFoto = Path.of("/Users/Waldir/Desktop/upload/catalogo", nomeArquivo);
//
//        System.out.println(arquivoFoto);
//        System.out.println(produtoFotoInputDTO.getDescricao());
//        System.out.println(produtoFotoInputDTO.getArquivo().getContentType());
//
//        try {
//            produtoFotoInputDTO.getArquivo().transferTo(arquivoFoto);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


    /**
     * Como na tabela FotoProduto o campo produto_id alem de identificador e a chave primaria, nao pode existir produto_id duplicado.
     * Na implementacao ao tentar gravar uma nova foto, se ja existir uma foto cadastrada ela sera excluida e a nova foto sera salva.
     * Na regra de negócio so e permitido gravar uma foto para cada produto.
     * @param restauranteId
     * @param produtoId
     * @param produtoFotoInputDTO
     * @return
     */
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)// Mepeado no contentType para receber apenas o MULTIPART_FORM_DATA_VALUE
    public FotoProdutoDTO atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid ProdutoFotoInputDTO produtoFotoInputDTO) {

        Produto produto = produtoService.buscarOuFalhar(restauranteId, produtoId);

        MultipartFile arquivo = produtoFotoInputDTO.getArquivo();

        FotoProduto foto = new FotoProduto();
        foto.setProduto(produto);
        foto.setDescricao(produtoFotoInputDTO.getDescricao());
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setNomeArquivo(arquivo.getOriginalFilename());

        FotoProduto fotoSalva = catalogoFotoProdutoService.salvar(foto);

        return fotoProdutoConverterDTO.converteEntidadeParaDto(fotoSalva);
    }
}






