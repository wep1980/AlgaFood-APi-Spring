package br.com.wepdev.domain.service;

import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class FluxoPedidoService {


	@Autowired
	private EmissaoPedidoService emissaoPedidoService;


	@Autowired
	private PedidoRepository pedidoRepository; // O Evento de envio de email, so vai funcionar se o metodo save() do repository for chamado




	@Transactional
	public void confirmar(String codigoPedido){
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
		pedido.confirmar();

		pedidoRepository.save(pedido); // O save foi necessario para que o evento de disparo de email funcione, ou qualquer outro tipo de evento que seja criado
	}


	@Transactional
	public void cancelar(String codigoPedido) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
        pedido.cancelar();

		pedidoRepository.save(pedido); // O save foi necessario para que o evento de disparo de email funcione, ou qualquer outro tipo de evento que seja criado
	}


	@Transactional
	public void entregar(String codigoPedido) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
        pedido.entregar();
	}



}
