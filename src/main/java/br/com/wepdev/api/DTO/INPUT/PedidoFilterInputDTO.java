package br.com.wepdev.api.DTO.INPUT;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * Classe que representa as propriedades que podem ser filtradas na consulta
 */
import java.time.OffsetDateTime;

@Setter
@Getter
public class PedidoFilterInputDTO {


    private Long clienteId;
    private Long restauranteId;

    @DateTimeFormat(iso = ISO.DATE_TIME) // Anotacao de formatacao de data e hora para o formato ISO -> 2019-11-01T00:00:00Z
    private OffsetDateTime dataCriacaoInicio; // Data de criação do pedido

    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime dataCriacaoFim;  // Data de criação do fim do pedido

}
