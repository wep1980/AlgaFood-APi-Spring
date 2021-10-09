package br.com.wepdev.domain.service;

import br.com.wepdev.domain.model.agregadosDTO.VendaDiaria;
import br.com.wepdev.domain.filter.VendaDiariaFilter;

import java.util.List;

public interface VendaQueryService {


    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro);
}
