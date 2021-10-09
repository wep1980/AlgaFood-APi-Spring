package br.com.wepdev.infrastructure.service;


import br.com.wepdev.domain.model.Pedido;
import br.com.wepdev.domain.model.agregadosDTO.VendaDiaria;
import br.com.wepdev.domain.filter.VendaDiariaFilter;
import br.com.wepdev.domain.service.VendaQueryService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
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
     * @param filtro
     * @return
     */
    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {

        // Obtendo uma instancia de criteria builder, usado para construir Querys, Predicates, funções de agregação, etc
        var builder = manager.getCriteriaBuilder();


       //VendaDiaria -> tipo de query retornada.
        var query = builder.createQuery(VendaDiaria.class);

        // O Pedido não sera retornado, apenas VendaDiaria
        var root = query.from(Pedido.class);

        /*
        Criando uma funcao date do MySql
        "date" -> nome da funcao no banco de dados MySql
        Date.class -> ao executar a funcao é esperado que o JPA converta o date do MySql em um LocalDate
        root.get("dataCriacao") -> argumento da funcao date
         */
        var functionDateDataCriacao = builder.function("date", Date.class, root.get("dataCriacao"));

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

        query.select(selection);
        query.groupBy(functionDateDataCriacao);

        return manager.createQuery(query).getResultList();
    }






















}
