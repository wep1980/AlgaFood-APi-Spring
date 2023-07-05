package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTOentrada.UsuarioInputDTO;
import br.com.wepdev.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class UsuarioInputConverterUsuario {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
    */
    @Autowired
    private ModelMapper modelMapper;



    /**
     * Metodo que transforma RestauranteInput para Restaurante, utilizado no
     * @param usuarioInput
     * @return
     */
    public Usuario converteInputParaEntidade(UsuarioInputDTO usuarioInput){
        return modelMapper.map(usuarioInput, Usuario.class);
    }


    /**
     * Metodo que copia as propriedades de RestauranteINPUT para Restaurante, utilizado no atualizar()
     * @param usuarioInput
     * @param usuario
     */
    public void copiaInputParaEntidade(UsuarioInputDTO usuarioInput, Usuario usuario){

        modelMapper.map(usuarioInput, usuario);

    }


}
