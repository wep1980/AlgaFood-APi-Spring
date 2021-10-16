package br.com.wepdev.domain.service;

import br.com.wepdev.domain.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class FluxoPedidoService {


	@Autowired
	private EmissaoPedidoService emissaoPedidoService;


	@Autowired
	private EnvioEmailService envioEmailService;



	@Transactional
	public void confirmar(String codigoPedido){
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
		pedido.confirmar();

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
                    / Aqui em desinario precisaria ser passado um Set() de destinatarios(), mas com a anotação do lombok colocado no Set, ele singularizou o metedo,
                    transformando de destinatarios() para destinatario(), ou seja para passar varios destinatarios é nessesario fazer :
                    destinatario("wepbike@gmail.com") e assim por diante.
                     */
				.destinatario(pedido.getCliente().getEmail())
				.build();



        envioEmailService.enviar(mensagem);
	}


	@Transactional
	public void cancelar(String codigoPedido) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
        pedido.cancelar();
	}


	@Transactional
	public void entregar(String codigoPedido) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
        pedido.entregar();
	}



}
