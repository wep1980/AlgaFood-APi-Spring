package br.com.wepdev.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

/**
 * Classe que customiza a serialização da paginação(Page)
 * JsonSerializer<Page<?>> -> serializador para qualquer tipo de pagina
 */
@JsonComponent // Componente que fornece uma implementacao de serializador e deserializador que deve ser registrado no jackson
public class PageJsonSerializer extends JsonSerializer<Page<?>> {



    @Override
    public void serialize(Page<?> objects, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject(); // Começa um objeto

//        jsonGenerator.writeObjectField("content", objects.getContent()); // escrevendo uma propriedade de um objeto, colocando o objeto dentro do content
//        jsonGenerator.writeNumberField("size", objects.getSize()); // Colocando dentro do size a quantidade de elementos por cada pagina
//        jsonGenerator.writeNumberField("totalElements", objects.getTotalElements()); // total de elementos no geral, exemplo: quantidade total de pedidos
//        jsonGenerator.writeNumberField("totalPages", objects.getTotalPages());// total de paginas
//        jsonGenerator.writeNumberField("number", objects.getNumber());// Pagina que se encontra no momento

        jsonGenerator.writeObjectField("conteudo", objects.getContent()); // escrevendo uma propriedade de um objeto, colocando o objeto dentro do content
        jsonGenerator.writeNumberField("elementos por pagina", objects.getSize()); // Colocando dentro do size a quantidade de elementos por cada pagina
        jsonGenerator.writeNumberField("total geral elementos", objects.getTotalElements()); // total de elementos no geral, exemplo: quantidade total de pedidos
        jsonGenerator.writeNumberField("total paginas", objects.getTotalPages());// total de paginas
        jsonGenerator.writeNumberField("pagina atual", objects.getNumber());// Pagina que se encontra no momento

        jsonGenerator.writeEndObject(); // finaliza um objeto

    }
}
