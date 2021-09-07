package br.com.wepdev;

/**
 * Interface que contem os grupos para validações, assim evita validações em propriedades que nao deve existir.
 */
public interface Grupos {

    /**
     *  Interface do grupo cadastroRestaurante para validacao.
     *  Tipo de marcação, delimita que algumas constraints com a proprieade (group =) faz parte desse grupo, exemplo :
     *
     * @NotBlank( group = Grupo.CadastroRestaurante.class)
     *  Id
     */
    public interface CadastroRestaurante {}
}
