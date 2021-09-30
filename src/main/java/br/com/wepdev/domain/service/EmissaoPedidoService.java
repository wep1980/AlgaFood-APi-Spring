package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.exception.PedidoNaoEncontradoException;
import br.com.wepdev.domain.exception.UsuarioNaoEncontradoException;
import br.com.wepdev.domain.model.Grupo;
import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.model.Usuario;
import br.com.wepdev.domain.repository.PedidoRepository;
import br.com.wepdev.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmissaoPedidoService {


	@Autowired
	private PedidoRepository pedidoRepository;



	public Pedido buscarOuFalhar(Long pedidoId) {
		return pedidoRepository.findById(pedidoId)
				.orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
	}

}
