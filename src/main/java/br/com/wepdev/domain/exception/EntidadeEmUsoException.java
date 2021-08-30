package br.com.wepdev.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe de exception customizada que extende de RuntimeException, uma exception em tempo de execução
 * @author Waldir
 *
 */
@ResponseStatus(value = HttpStatus.CONFLICT)  // O Retorno dessa classe de exception quando for lançada e de CONFLITO
public class EntidadeEmUsoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	
	public EntidadeEmUsoException(String mensagem) {
		super(mensagem); // Chama o construtor do RuntimeException passando a mensagem recebido por parametro
	}

}
