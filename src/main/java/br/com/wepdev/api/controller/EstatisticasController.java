package br.com.wepdev.api.controller;

import br.com.wepdev.domain.filter.VendaDiariaFilter;
import br.com.wepdev.domain.model.agregadosDTO.VendaDiaria;
import br.com.wepdev.domain.service.VendaQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/estatisticas")
public class EstatisticasController {


    @Autowired
    private VendaQueryService vendaQueryService;


    @GetMapping("/vendas-diarias")
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro){
        return vendaQueryService.consultarVendasDiarias(filtro);
    }
}
