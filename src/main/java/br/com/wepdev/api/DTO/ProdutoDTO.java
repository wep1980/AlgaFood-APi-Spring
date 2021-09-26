package br.com.wepdev.api.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoDTO {


    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean ativo;


}
