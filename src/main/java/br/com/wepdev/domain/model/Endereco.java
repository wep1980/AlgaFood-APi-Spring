package br.com.wepdev.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Embeddable // Classe com capacidade de ser incoporada em qualquer entidade
public class Endereco {
	
	
	@Column(name = "endereco_cep")
	private String cep;
	
	@Column(name = "endereco_logradouro")
	private String logradouro;
	
	@Column(name = "endereco_numero")
	private String numero;
	
	@Column(name = "endereco_complemento")
	private String complemento;
	
	@Column(name = "endereco_bairro")
	private String bairro;
	
	/*
	 * Por padrao todas as anotações terminadas com ToOne utilizam Eager, com Lazy as cidades so serao carregadas caso seja necessario.
	 * Como cidade possui um @JsonIgnore e em nenhum lugar esta sendo feito um getCidade().getQualquerMetodo(), nao vai mas ser feito o select cozinha
	 */
	@ManyToOne(fetch = FetchType.LAZY) // Muitos endereços para uma cidade
	@JoinColumn(name = "endereco_cidade_id")
	private Cidade cidade;

}
