package br.com.wepdev.api.controller.openapi;

import br.com.wepdev.api.DTO.GrupoDTO;
import br.com.wepdev.api.exceptionhandler.Problem;
import br.com.wepdev.api.inputDTO.GrupoInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@Api(tags = "Grupos")
public interface GrupoControllerOpenApi {


    @ApiOperation("Lista os grupos")
    public List<GrupoDTO> listar();



    @ApiOperation("Busca um grupo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "ID da grupo inválido", content = @Content(schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public GrupoDTO buscar(
            @ApiParam(value = "ID de um grupo", example = "1") Long grupoId);



    @ApiOperation("Cadastra um grupo")
    @ApiResponses({
            @ApiResponse(responseCode ="201", description = "Grupo cadastrado"),
    })
    public GrupoDTO adicionar(@ApiParam(name = "corpo", value = "Representação de um novo grupo") GrupoInputDTO grupoInput);



    @ApiOperation("Atualiza um grupo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grupo atualizado"),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public GrupoDTO atualizar(@ApiParam(value = "ID de um grupo", example = "1") Long grupoId,
            @ApiParam(name = "corpo", value = "Representação de um grupo com os novos dados")
                    GrupoInputDTO grupoInput);



    @ApiOperation("Exclui um grupo por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Grupo excluído"),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public void remover(@ApiParam(value = "ID de um grupo", example = "1") Long grupoId);


}
