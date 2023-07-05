package br.com.wepdev.api.converter;

import br.com.wepdev.api.DTOentrada.CidadeInputDTO;
import br.com.wepdev.domain.model.Cidade;
import br.com.wepdev.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Classe que contem os metodos que convertem os Inputs do controller para Entidades
 */
@Component
public class CidadeInputConverterCidade {


    /*
    Biblioteca que faz conversões de um tipo para outro.
    Essa biblioteca não é nativa do Spring, dentro do pacote core/modelmapper foi criada uma instancia dentro do Spring com @Bean
    */
    @Autowired
    private ModelMapper modelMapper;



    /**
     * Metodo que transforma CidadeInputDTO para Cidade, utilizado no
     * @param cidadeInput
     * @return
     */
    public Cidade converteInputParaEntidade(CidadeInputDTO cidadeInput){
        return modelMapper.map(cidadeInput, Cidade.class);
    }


    /**
     * Metodo que copia as propriedades de RestauranteINPUT para Restaurante, utilizado no atualizar()
     * @param cidadeInput
     * @param cidade
     */
    public void copiaInputParaEntidade(CidadeInputDTO cidadeInput, Cidade cidade){

        /**
         * E necessario instanciar uma nova cidade, para evitar o problema de no momento da copia, o jpa entender que exista uma tentativa de troca de id
         * no id da cidade, ao inves de um referencia de tipo de cidade para o Estado.
         * Para evitar org.hibernate.HibernateException: identifier of an instance of
         * com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
         */
        cidade.setEstado(new Estado());

        modelMapper.map(cidadeInput, cidade);

    }

















}
