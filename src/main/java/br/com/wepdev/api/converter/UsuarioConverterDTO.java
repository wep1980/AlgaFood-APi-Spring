package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.api.DTO.UsuarioDTO;
import br.com.wepdev.domain.model.Usuario;
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
public class UsuarioConverterDTO {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
     */
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Metodo que converte Estado em EstadoDTO
     * @param usuario
     * @return
     */
    public UsuarioDTO converteEntidadeParaDto(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioDTO.class);
    }


    /**
     * Metodo que converte uma lista de Estado em uma lista de Estado
     * @param usuarios
     * @return
     */
    public List<UsuarioDTO> converteListaEntidadeParaListaDto(Collection<Usuario> usuarios){

        // Convertendo uma lista de Usuarios para uma lista de UsuarioDTO
        return usuarios.stream().map(usuario -> converteEntidadeParaDto(usuario))
                .collect(Collectors.toList());
    }
}
