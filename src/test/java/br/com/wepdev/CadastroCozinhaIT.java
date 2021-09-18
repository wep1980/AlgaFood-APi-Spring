package br.com.wepdev;

import br.com.wepdev.domain.exception.CozinhaNaoEncontradaException;
import br.com.wepdev.domain.exception.EntidadeEmUsoException;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;
import br.com.wepdev.domain.service.CozinhaService;

//import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.equalTo;
import br.com.wepdev.util.DatabaseCleaner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;



/**
 * Classe de testes de integração e testes de APi
 *
 * Um Teste nao pode dependender da execução de um outro teste, os testes devem ser independentes
 */
//Fornece as funcionalidades do spring para os testes. webEnvironment -> levanta um servidor para uso dos testes, em uma porta aleatoria
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class) // levanta o contexto do spring na hora dos testes
@TestPropertySource("/application-test.properties")// Configurando o application-properties de teste
public class CadastroCozinhaIT {



    @LocalServerPort
    private int port; // Variavel que recebe o numero da porta que foi levantado pelo servidor para teste

//    // Instancia da classe flyWay, usada para ser feito o migrate(), que limpa e injeta os dados no banco de dados. Sera usado no metodo que é executado antes de cada teste
//    @Autowired
//    private Flyway flyway;

    @Autowired
    private DatabaseCleaner databaseCleaner; // Intancia da classe responsavel por limpar os dados do banco de dados

    @Autowired
    private CozinhaRepository cozinhaRepository;


    /**
     * Metodo executado antes de cada teste.
     * Evita repetição de codigo que era colocado em cada metodo de teste
     */
    @Before
    public void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Habilita o logging-> log , quando falha o teste. -- Para nao ficar duplicando em todos os testes
        RestAssured.port = port; // Porta gerada para os testes web. -- Para nao ficar duplicando em todos os testes
        RestAssured.basePath = "/cozinhas"; // Caminho. -- Para nao ficar duplicando em todos os testes

//        /**
//         * Executa o arquivo afeterMigrate.sql que esta dentro da pasta db.migration/testdata, esse arquivo limpa o banco e injeta eles novamente.
//         * afeterMigrate.sql -> E um arquivo de callBack, ja que ele zera o banco e reinsere os dados
//         */
//        flyway.migrate();

        databaseCleaner.clearTables(); // metodo que vai limpar os dados de todas as tabelas
        prepararDados();

    }


    @Test
    public void deveRetornarStatus200_QuandoConsultarCozinhas(){

        //RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Habilita o logging

        RestAssured.given() // Dado que
                    //.basePath("/cozinhas") // eu tenho basePath
                    //.port(port) // na porta
                    .accept(ContentType.JSON) // aceita o retorno em Json
                .when() // Quando
                    .get() // for feita uma requisição GET
                    .then() // Então
                    .statusCode(HttpStatus.OK.value()); // O status precisa ser 200
    }


    @Test
    public void deveConter2Cozinhas_QuandoConsultarCozinhas() {

        //RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Habilita o logging

        RestAssured.given() // Dado que
                //.basePath("/cozinhas") // eu tenho basePath
                //.port(port) // na porta
                .accept(ContentType.JSON) // aceita o retorno em Json
                .when() // Quando
                .get() // for feita uma requisição GET
                .then() // Então
                .body("", Matchers.hasSize(2)) // Verifica no corpo se tem 4 objs. Matchers -> biblioteca para inscrever expressoes com regras de correspondencia entre objetos
                .body("nome" , Matchers.hasItems("Australiana", "Americana")); // Verifica se no corpo os objetos possuem esses nomes

    }


    @Test
    public void deveRetornarStatus201_QuandoCadastrarCozinha(){
        RestAssured.given() // Dado que
                    .body("{ \"nome\": \"Australiana\" }") // no corpo
                    .contentType(ContentType.JSON) // Tipo de conteudo passado na requisição
                    .accept(ContentType.JSON) // aceita de volta um Json
                .when() // Quando
                    .post() // for feito um POST
                .then() // então
                    .statusCode(HttpStatus.CREATED.value()); // O status deve ser criado
    }


    @Test
    public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {

        RestAssured.given() // Dado que
                .pathParam("cozinhaId", 2) // o parametro de caminho, que é cozinhaId
                    .accept(ContentType.JSON) // aceita o retorno em Json
                .when() // Quando
                    .get("/{cozinhaId}") // for feita uma requisição GET
                .then() // Então
                .statusCode(HttpStatus.OK.value()) // // O status deve ser 200 ok
                .body("nome", equalTo("Americana")); // o nome deve Americana
    }


    @Test
    public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {

        RestAssured.given() // Dado que
                .pathParam("cozinhaId", 100) // o parametro de caminho, que é cozinhaId
                .accept(ContentType.JSON) // aceita o retorno em Json
                .when() // Quando
                .get("/{cozinhaId}") // for feita uma requisição GET
                .then() // Então
                .statusCode(HttpStatus.NOT_FOUND.value()); // // O status deve ser 200 ok
    }


    /**
     * metodo responsavel por inserir uma massa de dados para rodar os testes
     */
    private void prepararDados(){

        Cozinha cozinha1 = new Cozinha();
        cozinha1.setNome("Australiana");
        cozinhaRepository.save(cozinha1);

        Cozinha cozinha2 = new Cozinha();
        cozinha2.setNome("Americana");
        cozinhaRepository.save(cozinha2);

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
