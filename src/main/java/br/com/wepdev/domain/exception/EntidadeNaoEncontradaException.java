package br.com.wepdev.domain.exception;

/**
 * Classe de exception customizada que extende de RuntimeException, uma exception em tempo de execução
 * @author Waldir
 *
 */
public class EntidadeNaoEncontradaException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	
	public EntidadeNaoEncontradaException(String mensagem) {
		super(mensagem); // Chama o construtor do RuntimeException passando a mensagem recebida por parametro
	}

}
