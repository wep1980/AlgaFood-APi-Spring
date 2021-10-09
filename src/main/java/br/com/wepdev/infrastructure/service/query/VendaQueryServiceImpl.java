package br.com.wepdev.infrastructure.service.query;


import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.model.StatusPedido;
import br.com.wepdev.domain.model.agregadosDTO.VendaDiaria;
import br.com.wepdev.domain.filter.VendaDiariaFilter;
import br.com.wepdev.domain.service.VendaQueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class VendaQueryServiceImpl implements VendaQueryService {


    @PersistenceContext
    private EntityManager manager;

    /**
     * Esse metodo nao faz apenas pesquisas, mas tambem transformação de dados, nesse caso nao teremos transformação.
     * Utilizando criteria api do JPA.
     * Metodo com função de agregação.
     * O timeOffset passado no parametro refere se ao timeZone do local onde a api esta sendo utilizada, pois as datas sao gravadas no banco de dados em formato UTC 00:00,
     * no horario de brasilia é -03:00
     * @param filtro
     * @return
     */
    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {

        // Obtendo uma instancia de criteria builder, usado para construir Querys, Predicates, funções de agregação, etc
        var builder = manager.getCriteriaBuilder();


       //VendaDiaria -> tipo de query retornada.
        var query = builder.createQuery(VendaDiaria.class);

        // O Pedido não sera retornado, apenas VendaDiaria
        var root = query.from(Pedido.class);

        // Predicate e um filtro, funciona como se fosse um where
        var predicates = new ArrayList<Predicate>();

        /*
            ****** Melhoria na function para trabalhar com as datas no horario de brasilia de acordo com query SQL abaixo *****

        select date(convert_tz(p.data_criacao, '+00:00', '-03:00')) as data_criacao,
           count(p.id) as total_vendas,
           sum(p.valor_total) as total_faturado
       from pedido p
           where p.status in ('CONFIRMADO','ENTREGUE')
           group by date(convert_tz(p.data_criacao, '+00:00','-03:00'))

        Criando uma funcao date do MySql
        "convert_tz" -> nome da funcao no banco de dados MySql.
        Date.class -> ao executar a funcao é esperado que o JPA converta o date do MySql em um LocalDate.
        root.get("dataCriacao") -> argumento(Propriedade da classe Pedido) passado na funcao date.
        Adicionando um literal que se referente ao SQL acima a '+00:00', '-03:00'
        builder.literal("+00:00") -> UTC, forma gravada no banco de dados
        builder.literal(timeOffset) -> timeZone passado pelo consumidor da APi de acordo com seu horario local
         */
        var functionConvertTzDataCriacao = builder.function("convert_tz", Date.class,
                root.get("dataCriacao"),
                builder.literal("+00:00"),
                builder.literal(timeOffset));

        /*
        Criando uma funcao date do MySql
        "date" -> nome da funcao no banco de dados MySql
        Date.class -> ao executar a funcao é esperado que o JPA converta o date do MySql em um LocalDate
        root.get("dataCriacao") -> argumento(Propriedade da classe Pedido) passado na funcao date.


        ****** Melhoria na function para trabalhar com as datas no horario de brasilia de acordo com query SQL abaixo *****

        select date(convert_tz(p.data_criacao, '+00:00', '-03:00')) as data_criacao,
           count(p.id) as total_vendas,
           sum(p.valor_total) as total_faturado
       from pedido p
           where p.status in ('CONFIRMADO','ENTREGUE')
           group by date(convert_tz(p.data_criacao, '+00:00','-03:00'))
         */
        //var functionDateDataCriacao = builder.function("date", Date.class, root.get("dataCriacao"));
        var functionDateDataCriacao = builder.function("date", Date.class, functionConvertTzDataCriacao);// Uma funcao dentro de outra

        /*
        Criando uma seleção correpondendo a um construtor, o resultado da pesquisa sera utilizado para chamar o construtor da classe VendaDiaria, ou seja
        construa VendaDiaria a partir da seleção.

       select date(p.data_criacao) as data_criacao,
       count(p.id) as total_vendas,
       sum(p.valor_total) as total_faturado
       from pedido p
       group by date(p.data_criacao);

        --> Para a data e feito um tratamento diferente, pois no select acima a função date é uma função nativa do MySql para truncar a data.

        A ordem das propriedades decvem seguir a mesma do construtor de VendaDiaria.

        builder.count(root.get("id") -> fazendo um count(Somando a quantidade de pedidos do dia) no id do pedido.
        builder.sum(root.get("valorTotal") -> Somando o valorTotal dos pedidos de uma data.
        functionDateDataCriacao -> funcao criada acima, para a dataCriacao
         */
        var selection = builder.construct(VendaDiaria.class,
                functionDateDataCriacao,
                builder.count(root.get("id")),
                builder.sum(root.get("valorTotal")));

        //*** Adicionando predicates no arrayList de acordo com a regra de negocio ***

        if (filtro.getRestauranteId() != null) {  //Se tiver um restauranteId no filtro, adiciona ele no predicate
            // Adicionando um novo predicate, comparando a propriedade que vai ser consultada = "restaurante", o valor que sera filtrado = getRestauranteId()
            predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
        }

        if (filtro.getDataCriacaoInicio() != null) { //Se tiver uma DataCriacaoInicio no filtro, adiciona ele no predicate
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"),
                    filtro.getDataCriacaoInicio()));
        }

        if (filtro.getDataCriacaoFim() != null) { //Se tiver uma DataCriacaoFim no filtro, adiciona ele no predicate
            predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"),
                    filtro.getDataCriacaoFim()));
        }

        /*
        O status do pedidos tem que ser CONFIRMADO ou ENTREGUE.
        root.get("status").in -> No SQL seria algo como where p.status in ('CONFIRMADO','ENTREGUE')
         */
        predicates.add(root.get("status").in(
                StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));

        query.select(selection);
        query.where(predicates.toArray(new Predicate[0]));
        query.groupBy(functionDateDataCriacao);

        return manager.createQuery(query).getResultList();
    }






















}
