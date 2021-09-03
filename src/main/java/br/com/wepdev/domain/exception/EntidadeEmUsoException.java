package br.com.wepdev.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe de exception customizada que extende de NegocioException, uma exception em tempo de execução
 * @author Waldir
 *
 */
@ResponseStatus(value = HttpStatus.CONFLICT)  // 409 -> CONFLICT(a solicitação atual conflitou com o recurso que está no servidor)
public class EntidadeEmUsoException extends NegocioException {
	private static final long serialVersionUID = 1L;
	
	
	public EntidadeEmUsoException(String mensagem) {
		super(mensagem); // Chama o construtor do RuntimeException passando a mensagem recebido por parametro
	}

}
