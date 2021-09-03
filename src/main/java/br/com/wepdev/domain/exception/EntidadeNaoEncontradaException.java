package br.com.wepdev.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Classe de exception customizada que extende de NegocioException, uma exception em tempo de execução
 * E Lançada esse excessão quando uma cozinha não existe
 * @author Waldir
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)// 404 Not Found(o servidor não conseguiu encontrar o recurso solicitado)
public abstract class EntidadeNaoEncontradaException extends NegocioException{
	private static final long serialVersionUID = 1L;


	/*
	this -> esse construtor chama o construtor acima.
	O status 404 NOT_FOUND é o padrão, mas pode ser chamado um outro qualquer no service por exemplo, embora no service nao seja recomendado
	 */
	public EntidadeNaoEncontradaException(String mensagem) {
		super ( mensagem); // Chama o construtor do RuntimeException passando a mensagem recebida por parametro
	}


}
