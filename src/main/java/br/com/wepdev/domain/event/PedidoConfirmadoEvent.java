package br.com.wepdev.domain.event;

import br.com.wepdev.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Classe que representa um evento de um Pedido confirmado
 */
@Getter
@AllArgsConstructor // Gera construtor com as propriedades da classe
public class PedidoConfirmadoEvent {


    private Pedido pedido;
}
