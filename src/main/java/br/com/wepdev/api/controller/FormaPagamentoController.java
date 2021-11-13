package br.com.wepdev.api.controller;


import br.com.wepdev.api.DTO.FormaPagamentoDTO;

import br.com.wepdev.api.inputDTO.FormaPagamentoInputDTO;
import br.com.wepdev.api.converter.FormaPagamentoConverterDTO;
import br.com.wepdev.api.converter.FormaPagamentoInputConverterFormaPagamento;
import br.com.wepdev.domain.model.FormaPagamento;
import br.com.wepdev.domain.repository.FormaPagamentoRepository;
import br.com.wepdev.domain.service.FormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {


    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @Autowired
    private FormaPagamentoInputConverterFormaPagamento formaPagamentoInputConverterFormaPagamento;

    @Autowired
    private FormaPagamentoConverterDTO formaPagamentoConverterDTO;


    /**
     * Adionando o tipo de retorno como ResponseEntity, para poder alterar o cabeçalho da resposta e assim adicionar um cache de 10 segundos na requisição da representação
     * @return
     */
    @GetMapping
    public ResponseEntity<List<FormaPagamentoDTO>> listar(ServletWebRequest request) {

        /**
         * Depois ter habilitado o Etag no projeto, ele filtra as respostas dos endpoints e gera um hash da resposta adicionando o cabeçalho Etag.
         *
         * Deep ETags -> evita que o processamento completo seja feito do lado do servidor, caso a validação do Etag recebida no cabeçalho If-None-Match
         * confirme que não tenha nenhuma modificação no Etag. Ou seja o consumidor envia If-None-Match com etag que esta no cache, se o etag for igual ao da resposta
         * que servidor, nao precisa executar o processamento completo.
         *
         * O Obejtivo e economia de banda, processamento do servidor e processamento do banco de dados.
         *
         * Verifica se o valor que esta no cabeçalho do If-None-Matchetag do Etag e igual ao valor que seria passado na resposta.
         * Sera buscada a ultima data de atualização de FormaPagamento
         *
         */
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest()); // Desabilitando o filtro que cria o Etag para essa requisição

        String eTag = "0";

        OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();
        if(dataUltimaAtualizacao != null){
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond()); // Convertendo a data da ultima atualizacao para segundos e trasnformando o resultado em string
        }

        /*
        compara se o If-None-Matchetag e igual ao Etag passada. Pega o  If-None-Matchetag do cabeçalho da requisição e compara com o Etag
         */
        if(request.checkNotModified(eTag)){ // se nao foi modificado, ou seja se sao iguais
            return null;
        }


        List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();

        List<FormaPagamentoDTO> formasPagamentosDTO = formaPagamentoConverterDTO.converteListaEntidadeParaListaDto(todasFormasPagamentos);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())// Colocando a representação no cache da requisicao por 10 segundos
                .eTag(eTag) // Adionando o etag no cabeçalho da resposta
                .body(formasPagamentosDTO);
    }


    /**
     * Adionando o tipo de retorno como ResponseEntity, para poder alterar o cabeçalho da resposta e assim adicionar um cache de 10 segundos na requisição da representação
     *
     * Metodo que vai retornar um itag contendo o hash da resposta. o filtro para criação do itag foi habilitado aqui no spring
     * @return
     */
    @GetMapping("/{formaPagamentoId}")
    public ResponseEntity<FormaPagamentoDTO> buscar(@PathVariable Long formaPagamentoId) {

        FormaPagamento formaPagamento = formaPagamentoService.buscarOuFalhar(formaPagamentoId);

        FormaPagamentoDTO formaPagamentoDTO = formaPagamentoConverterDTO.converteEntidadeParaDto(formaPagamento);

        /*
        CacheControl.maxAge(10, TimeUnit.SECONDS) ->  Colocando a representação no cache da requisicao por 10 segundos.
        cachePrivate() -> permite o armazenamento do cache local, exemplo : no navegador. Nao permito o armazenamento compartilhado do cache, ou seja nao permite
        em um proxy, por exemplo.
        cachePublic() -> o cache pode ser armazenado tanto em cache local e em cache compartilhado(é o padrão)
         */
        return ResponseEntity.ok()
                //.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                //.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                //.cacheControl(CacheControl.noCache()) // Se a resposta for cacheada sera sempre necessario uma validação(Como se esse cache estivesse sempre em stale(velho))
                //.cacheControl(CacheControl.noStore()) // nenhum cache pode ser armazenado

                .body(formaPagamentoDTO);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoDTO adicionar(@RequestBody @Valid FormaPagamentoInputDTO formaPagamentoInput) {

        FormaPagamento formaPagamento = formaPagamentoInputConverterFormaPagamento.converteInputParaEntidade(formaPagamentoInput);

        formaPagamento = formaPagamentoService.salvar(formaPagamento);

        return formaPagamentoConverterDTO.converteEntidadeParaDto(formaPagamento);
    }


    @PutMapping("/{formaPagamentoId}")
    public FormaPagamentoDTO atualizar(@PathVariable Long formaPagamentoId, @RequestBody @Valid FormaPagamentoInputDTO formaPagamentoInput) {

        FormaPagamento formaPagamentoAtual = formaPagamentoService.buscarOuFalhar(formaPagamentoId);

        formaPagamentoInputConverterFormaPagamento.copiaInputParaEntidade(formaPagamentoInput, formaPagamentoAtual);

        formaPagamentoAtual = formaPagamentoService.salvar(formaPagamentoAtual);

        return formaPagamentoConverterDTO.converteEntidadeParaDto(formaPagamentoAtual);
    }


    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaPagamentoId) {

        formaPagamentoService.excluir(formaPagamentoId);
    }
}

