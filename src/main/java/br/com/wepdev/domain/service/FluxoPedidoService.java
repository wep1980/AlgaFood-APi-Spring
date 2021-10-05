package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.model.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class FluxoPedidoService {


	@Autowired
	private EmissaoPedidoService emissaoPedidoService;



	@Transactional
	public void confirmar(Long pedidoId){
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);
		pedido.confirmar();
	}


	@Transactional
	public void cancelar(Long pedidoId) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);
        pedido.cancelar();
	}


	@Transactional
	public void entregar(Long pedidoId) {
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);
        pedido.entregar();
	}



}
