package br.com.wepdev.api.controller;

import br.com.wepdev.api.DTO.FotoProdutoDTO;
import br.com.wepdev.api.converter.FotoProdutoConverterDTO;
import br.com.wepdev.api.inputDTO.ProdutoFotoInputDTO;
import br.com.wepdev.domain.exception.EntidadeNaoEncontradaException;
import br.com.wepdev.domain.model.FotoProduto;
import br.com.wepdev.domain.model.Produto;
import br.com.wepdev.domain.service.CatalogoFotoProdutoService;
import br.com.wepdev.domain.service.FotoStorageService;
import br.com.wepdev.domain.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {



    @Autowired
    private CatalogoFotoProdutoService catalogoFotoProdutoService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private FotoProdutoConverterDTO fotoProdutoConverterDTO;

    @Autowired
    private FotoStorageService fotoStorageService;


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
    public FotoProdutoDTO atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid ProdutoFotoInputDTO produtoFotoInputDTO) throws IOException {

        Produto produto = produtoService.buscarOuFalhar(restauranteId, produtoId);

        MultipartFile arquivo = produtoFotoInputDTO.getArquivo();

        FotoProduto foto = new FotoProduto();
        foto.setProduto(produto);
        foto.setDescricao(produtoFotoInputDTO.getDescricao());
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setNomeArquivo(arquivo.getOriginalFilename());

        FotoProduto fotoSalva = catalogoFotoProdutoService.salvar(foto, arquivo.getInputStream());

        return fotoProdutoConverterDTO.converteEntidadeParaDto(fotoSalva);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Retorna um Json, caso o consumidor da APi informar a Accept application/json
    public FotoProdutoDTO buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {

        FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);

        return fotoProdutoConverterDTO.converteEntidadeParaDto(fotoProduto);
    }


    /**
     * Classe que representa um InputStream de um recurso.
     *
     * Ao nao especificar o produces, qualquer tipo de MediaType que nao seja application/json vai cair neste endpoint.
     *
     * @RequestHeader(name = "accept") String acceptHeader -> Pega o que for especificado no accept do cabeçalho da requisição, que pode ser
     * uma lista, exp : image/jpeg,image/png.
     * (name = "accept") -> binding com String acceptHeader --- accept -> especificação do nome do header
     *
     * TIPOS DE MEDIATYPE passados no POSTMAN accept -> image/* - image/png - image/jpeg - image/gif - image/gif,image/jpeg,image/png
     */
    @GetMapping//(produces = MediaType.IMAGE_JPEG_VALUE)// Retorna um JPEG, caso o consumidor da APi informar a Accept application/jpeg
    public ResponseEntity<InputStreamResource> servirFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                                          @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {

        try {
        FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);

        /*
        parseMediaType() -> converte uma String em um tipo MediaType.
        MediaType == contentType -> são iguais
         */
        MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());// MediaType da foto armazenada no disco e cadastrada no banco de dados

        /*
        mediasTypes aceitas pelo consumidor da Api, lista com os mediaTypes.
        parseMediaTypes ->  converte uma lista de Strings em um tipo de lista de MediaTypes.
         */
        List<MediaType> mediatypesAceitas = MediaType.parseMediaTypes(acceptHeader);

        verificarCompatibilidadeMediaType(mediaTypeFoto, mediatypesAceitas);

        InputStream inputStream = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());

        return ResponseEntity.ok()
                .contentType(mediaTypeFoto) // Retorna no cabeçalho o contentType exato que esta armazenado
                .body(new InputStreamResource(inputStream));

        // Exception gerada caso a foto de um produto buscada nao exista no produto
        } catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();

        }

    }

    private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediatypesAceitas) throws HttpMediaTypeNotAcceptableException {

        /**
         * Verifica se dentro da lista de mediasTypes aceitas tem pelo ao menos uma mediaType compativel com a mediaType da foto
         */
        boolean compativel = mediatypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

        if(!compativel){
            // HttpMediaTypeNotAcceptableException -> exception tratada pelo exceptionHandler
            throw new HttpMediaTypeNotAcceptableException(mediatypesAceitas);
        }
    }


}






