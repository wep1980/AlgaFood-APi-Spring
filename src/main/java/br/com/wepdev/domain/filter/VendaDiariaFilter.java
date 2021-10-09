package br.com.wepdev.domain.filter;


import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@Setter
@Getter
public class VendaDiariaFilter {


    private Long restauranteId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // Anotacao de formatacao de data e hora para o formato ISO -> 2019-11-01T00:00:00Z
    private OffsetDateTime dataCriacaoInicio; // Data de criação do pedido

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime dataCriacaoFim;  // Data de criação do fim do pedido

}
