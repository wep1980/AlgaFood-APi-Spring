package br.com.wepdev.domain.model;

public enum StatusPedido {


    CRIADO("Criado"),
    CONFIRMADO("Confirmado"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");



    // Dessa forma pode ser passado uma descricao mais simples, LEGIVEL PARA HUMANOS, ao inves de passar os enums acima, que nesse caso sao simples.
    private String descricao;


    StatusPedido(String descricao){
        this.descricao = descricao;
    }


    public String getDescricao(){
        return this.descricao;
    }
}
