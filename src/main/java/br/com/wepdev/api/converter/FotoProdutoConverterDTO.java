package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.api.DTO.FotoProdutoDTO;
import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.model.FotoProduto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que contem os metodos que convertem de Entidades para DTO
 */
@Component
public class FotoProdutoConverterDTO {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Metodo que converte Estado em EstadoDTO
     * @param foto
     * @return
     */
    public FotoProdutoDTO converteEntidadeParaDto(FotoProduto foto) {
        return modelMapper.map(foto, FotoProdutoDTO.class);
    }

}
