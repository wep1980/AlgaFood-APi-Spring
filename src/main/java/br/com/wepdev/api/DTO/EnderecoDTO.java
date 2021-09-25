package br.com.wepdev.api.DTO;

import br.com.wepdev.domain.model.Cidade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
public class EnderecoDTO {


    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;

    private CidadeResumoDTO cidade;
}
