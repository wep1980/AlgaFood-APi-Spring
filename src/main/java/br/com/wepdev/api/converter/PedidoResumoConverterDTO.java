package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.PedidoDTO;
import br.com.wepdev.api.DTO.PedidoResumoDTO;
import br.com.wepdev.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que contem os metodos que convertem de Entidades para DTO
 */
@Component
public class PedidoResumoConverterDTO {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Metodo que converte Estado em EstadoDTO
     * @param pedido
     * @return
     */
    public PedidoResumoDTO converteEntidadeParaDto(Pedido pedido) {
        return modelMapper.map(pedido, PedidoResumoDTO.class);
    }


    /**
     * Metodo que converte uma lista de Estado em uma lista de Estado
     * @param pedidos
     * @return
     */
    public List<PedidoResumoDTO> converteListaEntidadeParaListaDto(Collection<Pedido> pedidos){

        // Convertendo uma lista de Usuarios para uma lista de UsuarioDTO
        return pedidos.stream().map(pedido -> converteEntidadeParaDto(pedido))
                .collect(Collectors.toList());
    }
}
