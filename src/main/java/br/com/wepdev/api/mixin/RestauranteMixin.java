package br.com.wepdev.api.mixin;

import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.model.Endereco;
import br.com.wepdev.domain.model.FormaPagamento;
import br.com.wepdev.domain.model.Produto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe mixin para configuracao das propriedades que existem em Restaurante com anotações do Jackson
 */
public abstract class RestauranteMixin {


    /**
     * No momento de serializar e desserializar(Usar no postman) um restaurante, ignora o nome de cozinha
     * serializar -> converte um objeto para um Json(GET)
     * desserializar -> converte o Json para um objeto
     * allowGetters = true -> permite que o nome apareça em GETs
     */
    @JsonIgnoreProperties(value = "nome" , allowGetters = true)
    private Cozinha cozinha;


    @JsonIgnore// Na hora de serializar(GET) a propriedade endereço sera ignorada
    private Endereco endereco;


    @JsonIgnore // Na hora de serializar(GET) a propriedade cozinha sera ignorada
    private List<FormaPagamento> formasPagamento;


    //@JsonIgnore// Na hora de serializar(GET) a propriedade cozinha sera ignorada
    private OffsetDateTime dataCadastro;


    //@JsonIgnore// Na hora de serializar(GET) a propriedade cozinha sera ignorada
    private OffsetDateTime dataAtualizacao;


    @JsonIgnore// Na hora de serializar(GET) a propriedade cozinha sera ignorada
    private List<Produto> produtos;
}
