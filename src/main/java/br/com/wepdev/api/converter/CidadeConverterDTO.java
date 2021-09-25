package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.domain.model.Cidade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que contem os metodos que convertem de Entidades para DTO
 */
@Component
public class CidadeConverterDTO {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Metodo que converte Estado em EstadoDTO
     * @param cidade
     * @return
     */
    public CidadeDTO converteEntidadeParaDto(Cidade cidade) {
        return modelMapper.map(cidade, CidadeDTO.class);
    }


    /**
     * Metodo que converte uma lista de Estado em uma lista de Estado
     * @param cidades
     * @return
     */
    public List<CidadeDTO> converteListaEntidadeParaListaDto(List<Cidade> cidades){

        // Convertendo uma lista de Restaurantes para uma lista de RestauranteDTO
        return cidades.stream().map(cidade -> converteEntidadeParaDto(cidade))
                .collect(Collectors.toList());
    }
}
