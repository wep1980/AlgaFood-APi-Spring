package br.com.wepdev.domain.model;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {


    CRIADO("Criado"),
    CONFIRMADO("Confirmado", CRIADO), // CRIADO e o Status anterior permido para chegar ao status confirmado
    ENTREGUE("Entregue", CONFIRMADO), // CONFIRMADO e o Status anterior permido para chegar ao status Entregue
    CANCELADO("Cancelado", CRIADO); // CRIADO e o Status anterior permido para chegar ao status Cancelado



    // Dessa forma pode ser passado uma descricao mais simples, LEGIVEL PARA HUMANOS, ao inves de passar os enums acima, que nesse caso sao simples.
    private String descricao;


    // Variavel que armazena os statusAnterios de cada Status atual
    private List<StatusPedido> statusAnteriores;


    /**
     * StatusPedido... statusAnteriores -> Pode ser passado apenas um status, nenhum ou mais de 1 status
     * @param descricao
     * @param statusAnteriores
     */
    StatusPedido(String descricao, StatusPedido... statusAnteriores){
        this.descricao = descricao;
        this.statusAnteriores = Arrays.asList(statusAnteriores);
    }

    /**
     * Metodo que verifica se a instancia do status atual nao pode ser alterada para o novo status.
     * Se o status atual for Entregue -> novoStatus, statusAnterios seria o CONFIRMADO, se nao for retorna true, ou seja nao permitido.
     * Se o status atual for Cancelado -> novoStatus, statusAnterios seria CRIADO, se nao for retorna true, ou seja nao permitido.
     * @param novoStatus
     * @return
     */
    public boolean naoPodeAlterarPara(StatusPedido novoStatus){

        /*
        Pega o status atual com o *this* e verifca os status anteriores, exemplo : Cancelado contem CRIADO.
        Se o novo status nao tiver na lista do status atual, ele retorna true, o que significa que nao pode.
         */
        return !novoStatus.statusAnteriores.contains(this);

    }


    public String getDescricao(){
        return this.descricao;
    }
}
