package br.com.wepdev.domain.model.agregadosDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * Classe que representa os dados agregados de vendas diarias
 */
@AllArgsConstructor // Lombok construtor com todos as propriedades da classe
@Setter
@Getter
public class VendaDiaria {


    // Nao foi possivel utilizar o LocalDate
    private Date data;

    private Long totalVendas;

    private BigDecimal totalFaturado;

}
