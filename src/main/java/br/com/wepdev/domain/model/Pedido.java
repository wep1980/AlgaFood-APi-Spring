package br.com.wepdev.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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

    @Enumerated(EnumType.STRING) // Evita erros na hora de converter a String para enumeração
    private StatusPedido status = StatusPedido.CRIADO;

    @CreationTimestamp
    private OffsetDateTime dataCriacao;

    private OffsetDateTime dataConfirmacao;

    private OffsetDateTime dataCancelamento;

    private OffsetDateTime dataEntrega;

    /**
     * (fetch = FetchType.LAZY) -> (Carregamento preguiçoso) So faz o select em forma de pagamento se for necessario,
     * Nem sempre que for buscado um pedidop e necessario a forma de pagamento. No DTO de PedidoResumoDTO nao tem forma de pagamento, entao nao e necessario
     * fazer um select nele
     */
    @ManyToOne(fetch = FetchType.LAZY)// muitos pedidos para 1 forma de pagamento
    @JoinColumn(nullable = false)
    private FormaPagamento formaPagamento;


    @ManyToOne // Muitos pedidos para 1 restaurante
    @JoinColumn(nullable = false)
    private Restaurante restaurante;

    @ManyToOne // Muitos pedidos para 1 cliente
    @JoinColumn(name = "usuario_cliente_id", nullable = false)
    private Usuario cliente;

    // Itens que estao sendo comprados
    @OneToMany(mappedBy = "pedido") // Um pedido para varios itens
    private List<ItemPedido> itens = new ArrayList<ItemPedido>();


    /**
     * Calcula o valor total de um pedido
     */
    public void calcularValorTotal() {
        this.subtotal = getItens().stream().map(item -> item.getPrecoTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotal = this.subtotal.add(this.taxaFrete);
    }


    public void definirFrete() {
        setTaxaFrete(getRestaurante().getTaxaFrete());
    }


    public void atribuirPedidoAosItens() {
        getItens().forEach(item -> item.setPedido(this));
    }

}
