package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTO.INPUT.CidadeInputDTO;
import br.com.wepdev.api.DTO.INPUT.GrupoInputDTO;
import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.model.Estado;
import br.com.wepdev.domain.model.Grupo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class GrupoInputConverterGrupo {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
    */
    @Autowired
    private ModelMapper modelMapper;



    /**
     * Metodo que transforma RestauranteInput para Restaurante, utilizado no
     * @param grupoInput
     * @return
     */
    public Grupo converteInputParaEntidade(GrupoInputDTO grupoInput){
        return modelMapper.map(grupoInput, Grupo.class);
    }


    /**
     * Metodo que copia as propriedades de RestauranteINPUT para Restaurante, utilizado no atualizar()
     * @param grupoInput
     * @param grupo
     */
    public void copiaInputParaEntidade(GrupoInputDTO grupoInput, Grupo grupo){

        modelMapper.map(grupoInput, grupo);

    }

















}
