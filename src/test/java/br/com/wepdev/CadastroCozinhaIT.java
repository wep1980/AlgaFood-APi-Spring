package br.com.wepdev;

import br.com.wepdev.domain.exception.CozinhaNaoEncontradaException;
import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.service.CozinhaService;

//import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;



/**
 * Classe de testes de integração e testes de APi
 */
//Fornece as funcionalidades do spring para os testes. webEnvironment -> levanta um servidor para uso dos testes, em uma porta aleatoria
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class) // levanta o contexto do spring na hora dos testes
public class CadastroCozinhaIT {

    @LocalServerPort
    private int port; // Variavel que recebe o numero da porta que foi levantado pelo servidor para teste

    @Test
    public void deveRetornarStatus200_QuandoConsultarCozinhas(){

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Habilita o logging

        RestAssured.given() // Dado que
                    .basePath("/cozinhas") // eu tenho basePath
                    .port(port) // na porta
                    .accept(ContentType.JSON) // aceita o retorno em Json
                .when() // Quando
                    .get() // for feita uma requisição GET
                    .then() // Então
                    .statusCode(HttpStatus.OK.value()); // O status precisa ser 200
    }

//    @Autowired
//    private CozinhaService cozinhaService;
//
//
//    @Test
//    public void deveAtribuirId_QuandocadastrarCozinhaComDadosCorretos(){
//
//        // Cenario
//        Cozinha novaCozinha = new Cozinha();
//        novaCozinha.setNome("Chinesa");
//
//        // Ação
//        novaCozinha = cozinhaService.salvar(novaCozinha);
//
//        // validação
//        assertThat(novaCozinha).isNotNull();
//        assertThat(novaCozinha.getId()).isNotNull();
//    }
//
//
//    @Test(expected = ConstraintViolationException.class) // Exception que tem que ser gerada
//    public void deveFalhar_QuandoCadastrarCozinhaSemNome(){
//
//        Cozinha novaCozinha = new Cozinha();
//
//        novaCozinha.setNome(null);
//
//        novaCozinha = cozinhaService.salvar(novaCozinha);
//    }
//
//
//    @Test(expected = EntidadeEmUsoException.class)
//    public void deveFalhar_QuandoExcluirCozinhaEmUso() {
//        cozinhaService.excluir(1L);
//    }
//
//
//    @Test(expected = CozinhaNaoEncontradaException.class)
//    public void deveFalhar_QuandoExcluirCozinhaInexistente() {
//        cozinhaService.excluir(100L);
//    }


}
