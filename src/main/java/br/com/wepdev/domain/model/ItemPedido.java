package br.com.wepdev.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
@Entity
public class ItemPedido {

    @EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
    private Long id;

    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;
    private Integer quantidade;
    private String observacao;

    @ManyToOne // Muitos itens de pedido para 1 pedido
    @JoinColumn(nullable = false)
    private Pedido pedido;

    @ManyToOne // Muitos itens de pedido para 1 produto
    @JoinColumn(nullable = false)
    private Produto produto;



    public void calcularPrecoTotal() {
        BigDecimal precoUnitario = this.getPrecoUnitario();
        Integer quantidade = this.getQuantidade();

        if (precoUnitario == null) {
            precoUnitario = BigDecimal.ZERO;
        }

        if (quantidade == null) {
            quantidade = 0;
        }
        this.setPrecoTotal(precoUnitario.multiply(new BigDecimal(quantidade)));
    }

}
