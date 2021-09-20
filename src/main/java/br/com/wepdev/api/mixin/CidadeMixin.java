package br.com.wepdev.api.mixin;

import br.com.wepdev.domain.model.Estado;
import br.com.wepdev.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Classe mixin para configuracao das propriedades que existem em Restaurante com anotações do Jackson
 */
public abstract class CidadeMixin {


    @JsonIgnoreProperties(value = "nome", allowGetters = true)
    private Estado estado;

}
