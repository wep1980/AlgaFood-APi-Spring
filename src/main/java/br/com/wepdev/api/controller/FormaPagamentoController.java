package br.com.wepdev.api.controller;


import br.com.wepdev.api.DTO.FormaPagamentoDTO;

import br.com.wepdev.api.DTO.INPUT.FormaPagamentoInputDTO;
import br.com.wepdev.api.converter.FormaPagamentoConverterDTO;
import br.com.wepdev.api.converter.FormaPagamentoInputConverterFormaPagamento;
import br.com.wepdev.domain.model.FormaPagamento;
import br.com.wepdev.domain.repository.FormaPagamentoRepository;
import br.com.wepdev.domain.service.FormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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



    @GetMapping
    public List<FormaPagamentoDTO> listar() {

        List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();

        return formaPagamentoConverterDTO.converteListaEntidadeParaListaDto(todasFormasPagamentos);
    }


    @GetMapping("/{formaPagamentoId}")
    public FormaPagamentoDTO buscar(@PathVariable Long formaPagamentoId) {

        FormaPagamento formaPagamento = formaPagamentoService.buscarOuFalhar(formaPagamentoId);

        return formaPagamentoConverterDTO.converteEntidadeParaDto(formaPagamento);
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

