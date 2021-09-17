package br.com.wepdev;

import br.com.wepdev.domain.exception.CozinhaNaoEncontradaException;
import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.service.CozinhaService;

//import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;



/**
 * Classe de testes de integração e testes de APi
 */
@RunWith(SpringRunner.class) // levanta o contexto do spring na hora dos testes
@SpringBootTest // Fornece as funcionalidades do spring para os testes
public class CadastroCozinhaIT {

    @Autowired
    private CozinhaService cozinhaService;


    @Test
    public void deveAtribuirId_QuandocadastrarCozinhaComDadosCorretos(){

        // Cenario
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");

        // Ação
        novaCozinha = cozinhaService.salvar(novaCozinha);

        // validação
        assertThat(novaCozinha).isNotNull();
        assertThat(novaCozinha.getId()).isNotNull();
    }


    @Test(expected = ConstraintViolationException.class) // Exception que tem que ser gerada
    public void deveFalhar_QuandoCadastrarCozinhaSemNome(){

        Cozinha novaCozinha = new Cozinha();

        novaCozinha.setNome(null);

        novaCozinha = cozinhaService.salvar(novaCozinha);
    }


    @Test(expected = EntidadeEmUsoException.class)
    public void deveFalhar_QuandoExcluirCozinhaEmUso() {
        cozinhaService.excluir(1L);
    }


    @Test(expected = CozinhaNaoEncontradaException.class)
    public void deveFalhar_QuandoExcluirCozinhaInexistente() {
        cozinhaService.excluir(100L);
    }


}
