package br.com.wepdev.api.openapi.controller;

import br.com.wepdev.api.DTO.FormaPagamentoDTO;
import br.com.wepdev.api.exceptionhandler.Problem;
import br.com.wepdev.api.DTOentrada.FormaPagamentoInputDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;

@Api(tags = "Formas de pagamento")
public interface FormaPagamentoControllerOpenApi {

    @ApiOperation("Lista as formas de pagamento")
    public ResponseEntity<List<FormaPagamentoDTO>> listar(ServletWebRequest request);

    @ApiOperation("Busca uma forma de pagamento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "ID da forma de pagamento inválido", content = @Content(schema = @Schema(implementation = Problem.class))),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada",content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public ResponseEntity<FormaPagamentoDTO> buscar(
            @ApiParam(value = "ID de uma forma de pagamento", example = "1")
                    Long formaPagamentoId,

            ServletWebRequest request);

    @ApiOperation("Cadastra uma forma de pagamento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Forma de pagamento cadastrada"),
    })
    public FormaPagamentoDTO adicionar(
            @ApiParam(name = "corpo", value = "Representação de uma nova forma de pagamento")
                    FormaPagamentoInputDTO formaPagamentoInput);

    @ApiOperation("Atualiza uma cidade por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Forma de pagamento atualizada"),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public FormaPagamentoDTO atualizar(
            @ApiParam(value = "ID de uma forma de pagamento", example = "1")
                    Long formaPagamentoId,

            @ApiParam(name = "corpo", value = "Representação de uma forma de pagamento com os novos dados")
                    FormaPagamentoInputDTO formaPagamentoInput);

    @ApiOperation("Exclui uma forma de pagamento por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Forma de pagamento excluída"),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrada", content = @Content(schema = @Schema(implementation = Problem.class)))
    })
    public void remover(Long formaPagamentoId);
}
