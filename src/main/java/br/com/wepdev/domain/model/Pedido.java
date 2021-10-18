package br.com.wepdev.domain.model;

import br.com.wepdev.domain.event.PedidoConfirmadoEvent;
import br.com.wepdev.domain.exception.NegocioException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * @Data -> Anotacao do LOMBOK que possui gets , sets , equals&HashCode e ToString.
 *
 * @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false) -> Habilita os campos explicidamente que serao utilizados no Equals e hashcode,
 * callSuper = false -> nao chama o metodo da super classe.
 *
 * AbstractAggregateRoot<Pedido> -> (Registra um evento) Pedido herda essa classe para que ela possa utilizar metodos de eventos,
 * nesse caso o envio de um email sera um evento toda vez que um pedido for confirmado
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
public class Pedido extends AbstractAggregateRoot<Pedido> {


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
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)//Um pedido para varios itens, CascadeType.ALL -> Salva os itens do pedido em cascata, junto com pedido
    private List<ItemPedido> itens = new ArrayList<ItemPedido>();

    /**
     * Campo criado para nao expor o id do produto, ja que o id revela a quantidade de pedidos existentes.
     * O codigo e gerado atraves de um UUID -- https://www.uuidgenerator.net/
     */
    private String codigo;

    /**
     * Calcula o valor total de um pedido
     */
    public void calcularValorTotal() {
        getItens().forEach(ItemPedido::calcularPrecoTotal);

        this.subtotal = getItens().stream()
                .map(item -> item.getPrecoTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotal = this.subtotal.add(this.taxaFrete);
    }


    public void definirFrete() {
        setTaxaFrete(getRestaurante().getTaxaFrete());
    }


    public void atribuirPedidoAosItens() {
        getItens().forEach(item -> item.setPedido(this));
    }


    public void confirmar(){
        setStatus(StatusPedido.CONFIRMADO);
        setDataConfirmacao(OffsetDateTime.now());

        /*
        this -> Instancia atual do Pedido que esta sendo confirmado.
        Registrando o evento que deve ser disparado assim que o objeto Pedido for salva no repository
         */
        registerEvent(new PedidoConfirmadoEvent(this));
    }


    public void entregar(){
        setStatus(StatusPedido.ENTREGUE);
        setDataEntrega(OffsetDateTime.now());
    }


    public void cancelar(){
        setStatus(StatusPedido.CANCELADO);
        setDataCancelamento(OffsetDateTime.now());
    }


    /**
     * Metodo setStatus() que altera o status atual de acordo com seu status anterior
     * @param novoStatus
     */
    private void setStatus(StatusPedido novoStatus){
        if(getStatus().naoPodeAlterarPara(novoStatus)){
            throw new NegocioException(String.format("Status do pedido %s não pode ser alterado de %s para %s",
                    getCodigo(),
                    getStatus().getDescricao(), // status atual do pedido
                    novoStatus.getDescricao()));
        }
        this.status = novoStatus;

    }


    /**
     * Gerando um codigo UUID e setando no campo codigo
     * @PrePersist -> antes de inserir um pedido executa esse metodo, metodo de callback do JPA
     */
    @PrePersist
    private void gerarCodigo(){
        setCodigo(UUID.randomUUID().toString());

    }

}
