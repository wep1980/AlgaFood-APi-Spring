package br.com.wepdev.api.openapi.controller;

import br.com.wepdev.api.DTO.CidadeDTO;
import br.com.wepdev.api.exceptionhandler.Problem;
import br.com.wepdev.api.inputDTO.CidadeInputDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;


/**
 * Interface que contem as anotações da documentação
 */
@Api(tags = "Cidades") // Controlador com recursos para utilização do swagger
public interface CidadeControllerOpenApi {



    @ApiOperation("Lista as cidades")
    public List<CidadeDTO> listar();




    /**
     * example = "1" -> Parametro que ja preenche na pagina HTML de documentação do swagger o numero 1 como exemplo do que deve ser preenchido.
     * @param cidadeId
     * @return
     */
    @ApiOperation("Busca uma cidade por ID")
    @ApiResponses({ // Adicionando respostas de erro na documentação com a representação no payload da resposta
            @ApiResponse(responseCode = "400", description = "ID da cidade inválido", content = @Content(schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public CidadeDTO buscar(@ApiParam(value = "ID de uma cidade", example = "1") Long cidadeId);





    @ApiOperation("Cadastra uma cidade")
    @ApiResponses({ // Adicionando respostas de erro na documentação com a representação no payload da resposta
            @ApiResponse(responseCode = "201", description = "Cidade cadastrada")
    })
    public CidadeDTO adicionar(@ApiParam(name = "corpo", value = "Representação de uma nova cidade") CidadeInputDTO cidadeInput);





    @ApiOperation("Atualiza uma cidade por ID")
    @ApiResponses({ // Adicionando respostas de erro na documentação com a representação no payload da resposta
            @ApiResponse(responseCode = "200", description = "Cidade atualizada"),
            @ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public CidadeDTO atualizar(
            @ApiParam(value = "ID de uma cidade") Long cidadeId,
            @ApiParam(name = "corpo", value = "Representação de uma cidade com os novos dados")
            CidadeInputDTO cidadeInput);





    @ApiOperation("Exclui uma cidade por ID")
    @ApiResponses({ // Adicionando respostas de erro na documentação com a representação no payload da resposta
            @ApiResponse(responseCode = "204", description = "Cidade excluída", content = @Content(schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public void remover(@ApiParam(value = "ID de uma cidade") Long cidadeId);

}
