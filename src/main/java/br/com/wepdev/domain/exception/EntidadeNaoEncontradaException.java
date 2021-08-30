package br.com.wepdev.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe de exception customizada que extende de RuntimeException, uma exception em tempo de execução
 * E Lançada esse excessão quando uma cozinha não existe
 * @author Waldir
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND) //, reason = "Entidade não encontrada") // O Retorno dessa classe de exception quando for lançada e 404 Not found
public class EntidadeNaoEncontradaException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	
	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem); // Chama o construtor do RuntimeException passando a mensagem recebida por parametro
	}

}
