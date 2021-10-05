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
	public void confirmar(String codigoPedido){
		Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
		pedido.confirmar();
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
