package br.com.wepdev.core.modelmapper;

import br.com.wepdev.api.DTO.EnderecoDTO;
import br.com.wepdev.api.inputDTO.ItemPedidoInputDTO;
import br.com.wepdev.domain.model.Endereco;
import br.com.wepdev.domain.model.ItemPedido;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {


    /**
     * Criando uma instancia de ModelMapper dentro do Spring
     * @return
     */
    @Bean
    public ModelMapper modelMapper(){
        var modelMapper = new ModelMapper();

//        modelMapper.createTypeMap(Restaurante.class, RestauranteDTO.class)
//                .addMapping(Restaurante::getTaxaFrete, RestauranteDTO::setTaxaFrete); // Mapeamento simples feito com referencia de metodos.

        // Adicionandp um mapeamento customizado , ignorando o mapeamento para o setId()
        modelMapper.createTypeMap(ItemPedidoInputDTO.class, ItemPedido.class)
                .addMappings(mapper -> mapper.skip(ItemPedido::setId));

        // Mapeamento de endereco para EnderecoDTO
        var enderecoToEnderecoDTOTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoDTO.class);

        //.<String> -> tipo do valor
        enderecoToEnderecoDTOTypeMap.<String>addMapping(enderecoOrigem -> enderecoOrigem.getCidade().getEstado().getNome(), // Origem de onde vem o valor
                (enderecoDtoDestino, valor) -> enderecoDtoDestino.getCidade().setEstado(valor)); // destino -> objeto do tipo enderecoDTO -- valor -> e o retorno da expressao lambda acima

        return modelMapper;
    }
}
