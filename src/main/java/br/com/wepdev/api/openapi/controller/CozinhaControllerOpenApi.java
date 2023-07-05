package br.com.wepdev.api.openapi.controller;

import br.com.wepdev.api.DTO.CozinhaDTO;
import br.com.wepdev.api.exceptionhandler.Problem;
import br.com.wepdev.api.DTOentrada.CozinhaInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Api(tags = "Cozinhas")
public interface CozinhaControllerOpenApi {


    @ApiOperation("Lista as cozinhas com paginação")
    public Page<CozinhaDTO> listarComPaginacao(Pageable pageable);

    @ApiOperation("Busca uma cozinha por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "ID da cozinha inválido", content = @Content(schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description = "Cozinha não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public CozinhaDTO buscar(
            @ApiParam(value = "ID de uma cozinha", example = "1")
                    Long cozinhaId);


    @ApiOperation("Cadastra uma cozinha")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cozinha cadastrada"),
    })
    public CozinhaDTO adicionar(
            @ApiParam(name = "corpo", value = "Representação de uma nova cozinha")
                    CozinhaInputDTO cozinhaInput);


    @ApiOperation("Atualiza uma cozinha por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cozinha atualizada"),
            @ApiResponse(responseCode = "404", description = "Cozinha não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public CozinhaDTO atualizar(
            @ApiParam(value = "ID de uma cozinha", example = "1")
                    Long cozinhaId,

            @ApiParam(name = "corpo", value = "Representação de uma cozinha com os novos dados")
                    CozinhaInputDTO cozinhaInput);

    @ApiOperation("Exclui uma cozinha por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cozinha excluída"),
            @ApiResponse(responseCode = "404", description = "Cozinha não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public void remover(
            @ApiParam(value = "ID de uma cozinha", example = "1")
                    Long cozinhaId);
}

