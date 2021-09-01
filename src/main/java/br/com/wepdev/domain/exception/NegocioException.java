package br.com.wepdev.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe de exception customizada que extende de RuntimeException, uma exception em tempo de execução
 * E Lançada esse excessão quando uma cozinha não existe
 * @author Waldir
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST) // 400 -> BAD_REQUEST (Erro do cliente).
public class NegocioException extends RuntimeException{
	private static final long serialVersionUID = 1L;


	public NegocioException(String mensagem) {
		super ( mensagem); // Chama o construtor do RuntimeException passando a mensagem recebida por parametro
	}


}
