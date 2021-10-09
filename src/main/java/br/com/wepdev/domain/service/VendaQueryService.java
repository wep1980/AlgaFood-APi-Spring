package br.com.wepdev.domain.service;

import br.com.wepdev.domain.model.agregadosDTO.VendaDiaria;
import br.com.wepdev.domain.filter.VendaDiariaFilter;

import java.util.List;

public interface VendaQueryService {

    /*
    O timeOffset passado refere se ao timezone do local onde a api esta sendo utilizada, pois as datas sao gravadas no banco de dados em formato UTC,
    no horario de brasilia e -03:00
     */
    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
}
