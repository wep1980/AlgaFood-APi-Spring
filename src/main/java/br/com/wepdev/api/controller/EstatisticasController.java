package br.com.wepdev.api.controller;

import br.com.wepdev.domain.filter.VendaDiariaFilter;
import br.com.wepdev.domain.model.agregadosDTO.VendaDiaria;
import br.com.wepdev.domain.service.VendaQueryService;
import org.springframework.beans.factory.annotation.Autowired;
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


    /**
     * O timeOffset passado por parametro refere se ao timeZone onde e utilizado a APi, pois as datas sao gravados no banco de dados em formato UTC 00:00,
     * no horario de brasilia e -03:00
     * (required = false) -> Se o consumidor da APi nao passar timeOffset a busca sera com o padrao UTC
     * @param filtro
     * @param timeOffset
     * @return
     */
    @GetMapping("/vendas-diarias")
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro,@RequestParam(required = false, defaultValue = "+00:00") String timeOffset){
        return vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
    }
}
