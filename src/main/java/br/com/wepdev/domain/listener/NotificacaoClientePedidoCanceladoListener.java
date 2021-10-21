package br.com.wepdev.domain.listener;

import br.com.wepdev.domain.event.PedidoCanceladoEvent;
import br.com.wepdev.domain.event.PedidoConfirmadoEvent;
import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Classe que fica escutando os eventos disparados de email
 */
@Component
public class NotificacaoClientePedidoCanceladoListener {


    @Autowired
    private EnvioEmailService envioEmailService;





    /**
     * @EventListener -> Anotacao que faz com que o metodo fique escutando, e toda vez que um pedido for cancelado ele entra em funcionamento.
     *
     * @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT) -> Anotacao que especifica que a transação toda so sera realizada, caso nao ocorra nenhum
     * problema no envio do email, se houver erro a operação interira sofre o rollback.
     *
     * @TransactionalEventListener -> Dessa forma o email so é enviado apos o pedido ser cancelado, e se houver erro no envio do email, o cliente ficara sem
     * receber mesmo. (Assim funciona a minha regra de negocio)
     */
    @TransactionalEventListener
    public void aoCancelarPedido(PedidoCanceladoEvent event){

        Pedido pedido = event.getPedido(); // Pegando a instancia de Pedido de dentro do evento

        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + "-" + " - Pedido cancelado")

                /**
                 * O conteudo do arquivo html sera processado por uma biblioteca chamada apache freemarker, que pega o html, o objeto java, e gera um output com html
                 * final
                 */
                .corpo("pedido-cancelado.html") // nome do arquivo do template que sera processado pela biblioteca freemarker

                /*
                recebe a variavel pedido, e o objeto pedido pra serem processados, no pedido-cancelado.html com o objeto Pedido é possivel pegar qualquer propriedade,
                no caso estamos pegando apenas o campo nome do cliente.
                Poderia pegar outros campos de Pedido como no exemplo: .variavel("nomeCliente", "Joao da silva") ou .variavel("valorTotal", variavelValue) ...etc
                 */
                .variavel("pedido", pedido)

                /*
                Aqui em desinario precisaria ser passado um Set() de destinatarios(), mas com a anotação do lombok colocado no Set, ele singularizou o metedo,
                transformando de destinatarios() para destinatario(), ou seja para passar varios destinatarios é nessesario fazer :
                destinatario("wepbike@gmail.com") e assim por diante.
                 */
                .destinatario(pedido.getCliente().getEmail())
                .build();

        envioEmailService.enviar(mensagem);

    }

}
