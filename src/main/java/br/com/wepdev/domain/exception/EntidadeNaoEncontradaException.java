package br.com.wepdev.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Classe de exception customizada que extende de RuntimeException, uma exception em tempo de execução
 * E Lançada esse excessão quando uma cozinha não existe
 * @author Waldir
 *
 */

/**
 * extends ResponseStatusException -> dessa forma epossivel alem de customizar a mensagem, pode customizar tambem o Codigo de Status HTTP
 */
//@ResponseStatus(value = HttpStatus.NOT_FOUND) //, reason = "Entidade não encontrada") // O Retorno dessa classe de exception quando for lançada e 404 Not found
public class EntidadeNaoEncontradaException extends ResponseStatusException {//RuntimeException{
	private static final long serialVersionUID = 1L;

    /*
    Construtor
     */
    public EntidadeNaoEncontradaException(HttpStatus status, String mensagem) {
		super(status, mensagem);
	}

	/*
	this -> esse construtor chama o construtor acima.
	O status 404 é o padrão, mas pode ser chamado um outro qualquer no service por exemplo, embora no service nao seja recomendado
	 */
	public EntidadeNaoEncontradaException(String mensagem) {
		this (HttpStatus.NOT_FOUND, mensagem); // Chama o construtor do RuntimeException passando a mensagem recebida por parametro
	}


}
