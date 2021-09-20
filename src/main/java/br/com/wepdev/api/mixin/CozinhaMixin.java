package br.com.wepdev.api.mixin;

import br.com.wepdev.domain.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe mixin para configuracao das propriedades que existem em Restaurante com anotações do Jackson
 */
public abstract class CozinhaMixin {


    @JsonIgnore
    private List<Restaurante> restaurantes;

}
