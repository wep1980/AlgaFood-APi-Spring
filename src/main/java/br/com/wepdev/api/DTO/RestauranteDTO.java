package br.com.wepdev.api.DTO;

import br.com.wepdev.domain.model.view.RestauranteView;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteDTO {

    @ApiModelProperty(example = "1")
    @JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class}) // Mostrando uma versão resumida com JSonView
    private Long id;

    @ApiModelProperty(example = "cozinha mineira")
    @JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class})
    private String nome;

    @ApiModelProperty(example = "12.00")
    @JsonView(RestauranteView.Resumo.class)
    private BigDecimal taxaFrete;

    @JsonView(RestauranteView.Resumo.class)
    private CozinhaDTO cozinha;

    private Boolean ativo;

    private Boolean aberto;

    private EnderecoDTO endereco;

}
