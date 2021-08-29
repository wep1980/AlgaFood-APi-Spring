package br.com.wepdev.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data // Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Habilita os campos explicidamente que serao utilizados no Equals e hashcode
@Entity
public class Pedido {


    @EqualsAndHashCode.Include // O Campo id sera o unico utilizado no equals e hashcode
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Quem gera a chave e o provedor do banco de dados
    private Long id;


    private BigDecimal subtotal;

    private BigDecimal taxaFrete;

    private BigDecimal valorTotal;

    @Embedded // Esta classe esta sendo incorporada em Restaurante
    private Endereco enderecoEntrega;

    private StatusPedido status;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    private LocalDateTime dataConfirmacao;

    private LocalDateTime dataCancelamento;

    private LocalDateTime dataEntrega;

    @ManyToOne// muitos pedidos para 1 forma de pagamento
    @JoinColumn(nullable = false)
    private FormaPagamento formaPagamento;


    @ManyToOne // Muitos pedidos para 1 restaurante
    @JoinColumn(nullable = false)
    private Restaurante restaurante;

    @ManyToOne // Muitos pedidos para 1 cliente
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    private Usuario cliente;

    @OneToMany(mappedBy = "pedido") // Um pedido para varios itens
    private List<ItemPedido> itens = new ArrayList<ItemPedido>();



}
