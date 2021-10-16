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

		var mensagem = EnvioEmailService.Mensagem.builder().assunto(pedido.getRestaurante().getNome() + "-" + " - Pedido confirmado")

						// Como no envio do corpo foi configurado como HTML, o <strong> que deixa em negrito, vai funcionar
						.corpo("O pedido de código <strong>" + pedido.getCodigo() + "</strong> foi confirmado!")

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
