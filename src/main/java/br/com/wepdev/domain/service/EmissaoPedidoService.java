package br.com.wepdev.domain.service;

import br.com.wepdev.domain.exception.NegocioException;
import br.com.wepdev.domain.exception.PedidoNaoEncontradoException;
import br.com.wepdev.domain.exception.UsuarioNaoEncontradoException;
import br.com.wepdev.domain.model.*;
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

	@Autowired
	private RestauranteService restauranteService;

	@Autowired
	private CidadeService cidadeService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private FormaPagamentoService formaPagamentoService;


	public Pedido buscarOuFalhar(Long pedidoId) {
		return pedidoRepository.findById(pedidoId)
				.orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
	}


	@Transactional
	public Pedido emitir(Pedido pedido) {
		validarPedido(pedido);
		validarItens(pedido);

		pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
		pedido.calcularValorTotal();

		return pedidoRepository.save(pedido);
	}

	private void validarPedido(Pedido pedido) {
		Cidade cidade = cidadeService.buscarOuFalhar(pedido.getEnderecoEntrega().getCidade().getId());
		Usuario cliente = usuarioService.buscarOuFalhar(pedido.getCliente().getId());
		Restaurante restaurante = restauranteService.buscarOuFalhar(pedido.getRestaurante().getId());
		FormaPagamento formaPagamento = formaPagamentoService.buscarOuFalhar(pedido.getFormaPagamento().getId());

		pedido.getEnderecoEntrega().setCidade(cidade);
		pedido.setCliente(cliente);
		pedido.setRestaurante(restaurante);
		pedido.setFormaPagamento(formaPagamento);

		if (restaurante.naoAceitaFormaPagamento(formaPagamento)) {
			throw new NegocioException(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.",
					formaPagamento.getDescricao()));
		}
	}

	private void validarItens(Pedido pedido) {
		pedido.getItens().forEach(item -> {
			Produto produto = produtoService.buscarOuFalhar(
					pedido.getRestaurante().getId(), item.getProduto().getId());

			item.setPedido(pedido);
			item.setProduto(produto);
			item.setPrecoUnitario(produto.getPreco());
		});
	}

}
