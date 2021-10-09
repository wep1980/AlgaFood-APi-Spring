package br.com.wepdev.api.controller;

import br.com.wepdev.domain.filter.VendaDiariaFilter;
import br.com.wepdev.domain.model.agregadosDTO.VendaDiaria;
import br.com.wepdev.domain.service.VendaQueryService;
import br.com.wepdev.domain.service.VendaReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/estatisticas")
public class EstatisticasController {


    @Autowired
    private VendaQueryService vendaQueryService;

    @Autowired
    private VendaReportService vendaReportService;


    /**
     * O timeOffset passado por parametro refere se ao timeZone onde e utilizado a APi, pois as datas sao gravados no banco de dados em formato UTC 00:00,
     * no horario de brasilia e -03:00
     * (required = false) -> Se o consumidor da APi nao passar timeOffset a busca sera com o padrao UTC
     *
     * produces = MediaType.APPLICATION_JSON_VALUE ->  Metodo chamado quando o consumidor da API aceita application JSON, esse o padrão
     *
     * @param filtro
     * @param timeOffset
     * @return
     */
    @GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro,@RequestParam(required = false, defaultValue = "+00:00") String timeOffset){
        return vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
    }


    /**
     * produces = MediaType.APPLICATION_PDF_VALUE -> Produz o tipo de media PDF
     * No postman no campo Accept deve passado : application/pdf
     *
     * ResponseEntity<byte[]> Retorna com ResponseEntity um array de byte
     */
    @GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> consultarVendasDiariasPdf(VendaDiariaFilter filtro, @RequestParam(required = false, defaultValue = "+00:00") String timeOffset){

        byte[] bytesPdf = vendaReportService.emitirVendasDiarias(filtro, timeOffset);

        var headers = new HttpHeaders();

        /**
         * Adicionando um cabeçalho.
         * attachment -> indica que o conteudo retornado no body() e para ser baixado(download) pelo cliente, nao exibido pelo navegador.
         * filename=vendas-diarias.pdf -> nome do arquivo sugerido para ser salvo localmente
         */
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");

        return ResponseEntity.ok() // Retornando codigo 200 ok
                .contentType(MediaType.APPLICATION_PDF) // Resposta HTTP com contentType APPLICATION_PDF)
                .headers(headers)
                .body(bytesPdf);
    }

}
