package br.com.wepdev.api.openapi.model;

import br.com.wepdev.api.DTO.CozinhaDTO;
import io.swagger.annotations.ApiModel;


/**
 * Classe criada para fins de documentação
 */
@ApiModel("PedidosResumoModel") // Alterando o nome da classe para PedidosResumoModel na documentação -- http://api.algafood.local:8080/swagger-ui/index.html#
public class PedidosResumoModelOpenApi extends PagedModelOpenApi<CozinhaDTO>{


}
