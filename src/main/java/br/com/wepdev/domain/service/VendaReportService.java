package br.com.wepdev.domain.service;


import br.com.wepdev.domain.filter.VendaDiariaFilter;

public interface VendaReportService {


    // Retorna um array de bytes, metodo para gerar relatorio de vendas
    byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset);

}
