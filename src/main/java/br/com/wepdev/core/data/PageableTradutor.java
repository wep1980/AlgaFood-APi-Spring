package br.com.wepdev.core.data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe utilitaria, pode ser utilizada em qualquer projeto
 */
public class PageableTradutor {


    /**
     * Metodo statico(a classe nao precisa ser instanciada para o metodo ser utilizado) que traduz o as propriedades enviadas no pageable(Paginacao) pelo consumidor da api
     * no momento de fazer uma ordenação(sort) e traduz pelas propriedades corretas
     * @param pageable
     * @param fieldsMapping
     * @return
     */
    public static Pageable tradutor(Pageable pageable, Map<String, String> fieldsMapping){
        //System.out.println(pageable.getSort()); // Print para ver o ta chegando no pageable vindo do postman(consumidor da APi)

       var orders = pageable.getSort().stream() // Na interação com sort ele retorna um tipo Order

               /*
               Se for passado um nome de propriedade que nao existe no sort(propriedade que sera ordenada passada pelo consumidor da Api) que nao exite no mapeamento
               ela sera ignorada
                */
                .filter(order -> fieldsMapping.containsKey(order.getProperty()))

               // Retorna um novo stream de Sort.Order, pegando a getDirection(direção da ordenação) recebido no argumento e instanciando um novo Sort.Order
                .map(order -> new Sort.Order(order.getDirection(),

                        /*
                        fieldsMapping -> segundo argumento recebido no metodo, o dePara, order.getProperty() -> pega o nome da propriedade referente ao nomeCliente , que é
                        cliente.nome
                         */
                        fieldsMapping.get(order.getProperty())))
                .collect(Collectors.toList()); // retorna a lista de Sort.Orders com as orders corretas

        /*
        Instanciando um novo Pageable com metodo statico of(), passando o que ja existia dentro do proprio pageable, Instanciando um novo Sort com by(orders) passando
        as orders(Os propriedades para ordenação)
         */
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
    }
}
