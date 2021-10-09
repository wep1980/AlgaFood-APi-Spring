package br.com.wepdev.api.converter;

import br.com.wepdev.api.inputDTO.PedidoInputDTO;
import br.com.wepdev.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class PedidoInputConverterPedido {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
    */
    @Autowired
    private ModelMapper modelMapper;



    /**
     * Metodo que transforma RestauranteInput para Restaurante, utilizado no
     * @param pedidoInputDTO
     * @return
     */
    public Pedido converteInputParaEntidade(PedidoInputDTO pedidoInputDTO){
        return modelMapper.map(pedidoInputDTO, Pedido.class);
    }


    /**
     * Metodo que copia as propriedades de RestauranteINPUT para Restaurante, utilizado no atualizar()
     * @param pedidoInputDTO
     * @param pedido
     */
    public void copiaInputParaEntidade(PedidoInputDTO pedidoInputDTO, Pedido pedido){

        modelMapper.map(pedidoInputDTO, pedido);

    }

















}
