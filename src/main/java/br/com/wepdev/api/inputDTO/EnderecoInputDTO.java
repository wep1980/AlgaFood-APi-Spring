package br.com.wepdev.api.inputDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Vamos imaginar que essa api ja estar em produção e ja existem cadastros feitos no banco de dados onde informar o endereço nao era obrigatorio, mas a partir
 * de agora de acordo com a nova regra, vai passar a ser obrigatorio, entao para evitar erros com os cadastros que ja existem sem o endereço e tambem
 * o processo de ter que regularizar os cadastros que ja existem sem o endereço, esse InputDTO
 * tera validaçoes na qual obriga os novos cadastros de restaurantes ter endereço.
 */
@Setter
@Getter
public class EnderecoInputDTO {

    @ApiModelProperty(example = "38400-000", required = true)
    @NotBlank
    private String cep;

    @ApiModelProperty(example = "Rua Floriano Peixoto", required = true)
    @NotBlank
    private String logradouro;

    @ApiModelProperty(example = "1500", required = true)
    @NotBlank
    private String numero;

    @ApiModelProperty(example = "Apto 901")
    private String complemento;

    @ApiModelProperty(example = "Centro", required = true)
    @NotBlank
    private String bairro;

    @Valid // Validar as propriedades dentro de cidadeIdInput
    @NotNull // Uma instancia de cidadeIdInput deve existir
    private CidadeIdInputDTO cidade;
}
