package br.com.wepdev.domain.listener;

import br.com.wepdev.domain.event.PedidoConfirmadoEvent;
import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Classe que fica escutando os eventos disparados de email
 */
@Component
public class NotificacaoClientePedidoConfirmadoListener {


    @Autowired
    private EnvioEmailService envioEmailService;



    @EventListener // Anotacao que faz com que o metodo fique escutando, e toda vez que um pedido for confirmado ele entra em funcionamento
    public void aoConfirmarPedido(PedidoConfirmadoEvent event){

        Pedido pedido = event.getPedido(); // Pegando a instancia de Pedido de dentro do evento

        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + "-" + " - Pedido confirmado")

                /**
                 * O conteudo do arquivo html sera processado por uma biblioteca chamada apache freemarker, que pega o html, o objeto java, e gera um output com html
                 * final
                 */
                .corpo("pedido-confirmado.html") // nome do arquivo do template que sera processado pela biblioteca freemarker

                /*
                recebe a variavel pedido, e o objeto pedido pra serem processados, no pedido-confirmado.html com o objeto Pedido é possivel pegar qualquer propriedade,
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
