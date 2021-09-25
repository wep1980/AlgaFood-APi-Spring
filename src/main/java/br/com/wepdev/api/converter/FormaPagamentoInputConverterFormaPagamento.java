package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.INPUT.CidadeINPUT;
import br.com.wepdev.api.DTO.INPUT.FormaPagamentoINPUT;
import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.model.Estado;
import br.com.wepdev.domain.model.FormaPagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class FormaPagamentoInputConverterFormaPagamento {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
    */
    @Autowired
    private ModelMapper modelMapper;



    /**
     * Metodo que transforma RestauranteInput para Restaurante, utilizado no
     * @param formaPagInput
     * @return
     */
    public FormaPagamento converteInputParaEntidade(FormaPagamentoINPUT formaPagInput){
        return modelMapper.map(formaPagInput, FormaPagamento.class);
    }


    /**
     * Metodo que copia as propriedades de RestauranteINPUT para Restaurante, utilizado no atualizar()
     * @param formaPagInput
     * @param formaPagamento
     */
    public void copiaInputParaEntidade(FormaPagamentoINPUT formaPagInput, FormaPagamento formaPagamento){

        modelMapper.map(formaPagInput, formaPagamento);

    }

















}
