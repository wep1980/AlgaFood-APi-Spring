package br.com.wepdev.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import br.com.wepdev.domain.model.Cozinha;
import lombok.Data;
import lombok.NonNull;

@Data // gera tambem construtores para propriedades obrigatorias, alem de gatters , setters e etc
@JacksonXmlRootElement(localName = "cozinhas") // Customiza o nome do elemento raiz na serializao XML, altera o nome que seria CozinhasXmlWrapper(nome da classe) para "cozinhas"
public class CozinhasXmlWrapper {
	
	
	@JacksonXmlProperty(localName = "cozinha") // alterando o nome na serializacao na hora de exibicao(POSTMAN) ficara assim <cozinha>
	@JacksonXmlElementWrapper(useWrapping = false)// Desabilitando um dos elementos de visualiazação da serialização(POSTMAN)
	@NonNull // Anotacao do lombok de propriedade obrigatoria para geracao de construtores
	private List<Cozinha> cozinha;

}
