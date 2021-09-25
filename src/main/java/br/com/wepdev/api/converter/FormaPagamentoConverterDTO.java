package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.api.DTO.FormaPagamentoDTO;
import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.model.FormaPagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que contem os metodos que convertem de Entidades para DTO
 */
@Component
public class FormaPagamentoConverterDTO {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Metodo que converte FormaPagamento em FormaPagamentoDTO
     * @param formaPagamento
     * @return
     */
    public FormaPagamentoDTO converteEntidadeParaDto(FormaPagamento formaPagamento) {
        return modelMapper.map(formaPagamento, FormaPagamentoDTO.class);
    }


    /**
     * Metodo que converte uma lista de FormaPagamento em uma lista de FormaPagamentoDTO
     * @param formaPagamentos
     * @return
     */
    public List<FormaPagamentoDTO> converteListaEntidadeParaListaDto(List<FormaPagamento> formaPagamentos){

        // Convertendo uma lista de formaPagamentos para uma lista de formapagamentosDTO
        return formaPagamentos.stream().map(formaPagamento -> converteEntidadeParaDto(formaPagamento))
                .collect(Collectors.toList());
    }
}
