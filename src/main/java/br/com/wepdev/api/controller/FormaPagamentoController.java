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

import javax.validation.Valid;
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
    public ResponseEntity<List<FormaPagamentoDTO>> listar() {

        List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();

        List<FormaPagamentoDTO> formasPagamentosDTO = formaPagamentoConverterDTO.converteListaEntidadeParaListaDto(todasFormasPagamentos);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))// Colocando a representação no cache da requisicao por 10 segundos
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

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS)) // Colocando a representação no cache da requisicao por 10 segundos
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

