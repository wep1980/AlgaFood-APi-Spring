package br.com.wepdev.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe de exception customizada que extende de EntidadeNaoEncontradaExceprion que foi criada por mim, uma exception em tempo de execução.
 * E Lançada esse excessão quando um Estado não existe, é um tipo de excessao mais especifica, cabe a vc decidir a necessidade de granularidade nas excessoes
 * @author Waldir
 *
 */
//@ResponseStatus(value = HttpStatus.NOT_FOUND)// 404 Not Found(o servidor não conseguiu encontrar o recurso solicitado) -> Foi retirado pois a EntidadeNaoEncontradaException ja possui o NOT_FOUND
// O HTTP Status esta sendo passado dentro da classe global ApiExceptionHandler
public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException{
	private static final long serialVersionUID = 1L;



	public EstadoNaoEncontradoException(String mensagem) {
		super ( mensagem); // Chama o construtor do RuntimeException passando a mensagem recebida por parametro
	}

	/**
	 * this() -> Chama o construtor acima
	 * * @param estadoId
	 */
	public EstadoNaoEncontradoException(Long estadoId) {
		this(String.format("Não existe um cadastro de estado com código %d", estadoId));
	}
}
