package br.com.wepdev.api.converter;


import br.com.wepdev.api.DTO.PermissaoDTO;
import br.com.wepdev.domain.model.Permissao;
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
public class PermissaoConverterDTO {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
     */
    @Autowired
    private ModelMapper modelMapper;


    public PermissaoDTO converteEntidadeParaDto(Permissao permissao) {
        return modelMapper.map(permissao, PermissaoDTO.class);
    }



    public List<PermissaoDTO> converteListaEntidadeParaListaDto(Collection<Permissao> permissoes){

        // Convertendo uma lista de Restaurantes para uma lista de RestauranteDTO
        return permissoes.stream().map(permissao -> converteEntidadeParaDto(permissao))
                .collect(Collectors.toList());
    }
}
