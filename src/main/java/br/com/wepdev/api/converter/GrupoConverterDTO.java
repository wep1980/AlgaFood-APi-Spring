package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.api.DTO.GrupoDTO;
import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.model.Grupo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que contem os metodos que convertem de Entidades para DTO
 */
@Component
public class GrupoConverterDTO {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Metodo que converte Estado em EstadoDTO
     * @param grupo
     * @return
     */
    public GrupoDTO converteEntidadeParaDto(Grupo grupo) {
        return modelMapper.map(grupo, GrupoDTO.class);
    }


    /**
     * Metodo que converte uma lista de Estado em uma lista de Estado
     * @param grupos
     * @return
     */
    public List<GrupoDTO> converteListaEntidadeParaListaDto(List<Grupo> grupos){

        // Convertendo uma lista de Restaurantes para uma lista de RestauranteDTO
        return grupos.stream().map(grupo -> converteEntidadeParaDto(grupo))
                .collect(Collectors.toList());
    }
}
