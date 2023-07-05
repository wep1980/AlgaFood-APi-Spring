package br.com.wepdev;

import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;

//import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.equalTo;
import br.com.wepdev.util.DatabaseCleaner;
import br.com.wepdev.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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


/**
 * Classe de testes de integração e testes de APi
 *
 * Um Teste nao pode dependender da execução de um outro teste, os testes devem ser independentes
 */
//Fornece as funcionalidades do spring para os testes. webEnvironment -> levanta um servidor para uso dos testes, em uma porta aleatoria
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class) // levanta o contexto do spring na hora dos testes
@TestPropertySource("/application-test.properties")// Configurando o application-test.properties de teste, onde contem as configs do banco de dados de teste
public class CadastroCozinhaIT { // As Classes de teste de integração devem seguir o padrão de nome IT, para que possa ser utilizado o plugin do maven-failsafe. Para executar os testes por via de comando com maven é : mvnw verify


    @LocalServerPort
    private int port; // Variavel que recebe o numero da porta que foi levantado pelo servidor para teste

    @Autowired // so e possivel injetar essa Classe pq ela foi criada com @componnet do spring
    private DatabaseCleaner databaseCleaner; // Intancia da classe responsavel por limpar os dados do banco de dados

    @Autowired
    private CozinhaRepository cozinhaRepository;


    private static final int COZINHA_ID_INEXISTENTE = 100;

    private Cozinha cozinhaAmericana; // Variavel de instancia da classe cozinha, que armazena os dados da cozinhaAmericana que e adicionada no metodo prepararDados();
    private int quantidadeCozinhasCadastradas; // Registra a quantidade cozinhas cadastradas
    private String jsonCorretoCozinhaChinesa;



    /**
     * Metodo executado antes de cada teste. Metodo de callBack
     * Evita repetição de codigo que era colocado em cada metodo de teste
     */
    @Before
    public void setUp(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Habilita o logging-> log , quando falha o teste. -- Para nao ficar duplicando em todos os testes
        RestAssured.port = port; // Porta gerada para os testes web. -- Para nao ficar duplicando em todos os testes
        RestAssured.basePath = "/cozinhas"; // Caminho. -- Para nao ficar duplicando em todos os testes

        // Classe de utilidades que contem o metodo que pega o valor do json, de dentro de um arquivo json qualquer, nesse caso o arquivo é cozinha-chinesa.json
        jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource(
                "/json/correto/cozinha-chinesa.json");

        // Antes de cada teste e feito a limpeza do banco e uma nova inserção
        databaseCleaner.clearTables(); // metodo que vai limpar os dados de todas as tabelas
        prepararDados();
    }


    /*
        TESTE DE API
        -> Biblioteca utilizada  io.rest-assured
     */
    @Test
    public void deveRetornarStatus200_QuandoConsultarCozinhas(){

        //RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Habilita o logging da requisição e da resposta

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
    public void deveRetornarQuantidadeCorretaDeCozinhas_QuandoConsultarCozinhas() {

        //RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Habilita o logging

        RestAssured.given() // Dado que
                //.basePath("/cozinhas") // eu tenho basePath
                //.port(port) // na porta
                      .accept(ContentType.JSON) // aceita o retorno em Json
                .when() // Quando
                      .get() // for feita uma requisição GET
                .then() // Então
                      .body("", Matchers.hasSize(quantidadeCozinhasCadastradas)) // Verifica no corpo se tem a quantidade de (Objetos)cozinhas correta.
                       // Matchers -> biblioteca para inscrever expressoes com regras de correspondencia entre objetos
                      .body("nome" , Matchers.hasItems("Tailandesa", "Americana")); // Verifica se no corpo os objetos possuem esses nomes

    }


    @Test
    public void deveRetornarStatus201_QuandoCadastrarCozinha(){
        RestAssured.given() // Dado que
                    //.body("{ \"nome\": \"Australiana\" }") // no corpo -> Refatorado na linha abaixo
                .body(jsonCorretoCozinhaChinesa) // Pegando o valor de dentro do arquivo .json, Dentro do metodo que inicia sempre antes de cada metodo de teste,
                                                 // e feita uma chamada no metodo responsavel por ler esse arquivo .json
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
                    .pathParam("cozinhaId", cozinhaAmericana.getId()) // o parametro de caminho, que é cozinhaId
                    .accept(ContentType.JSON) // aceita o retorno em Json
                .when() // Quando
                    .get("/{cozinhaId}") // for feita uma requisição GET
                .then() // Então
                    .statusCode(HttpStatus.OK.value()) // // O status deve ser 200 ok
                    .body("nome", equalTo(cozinhaAmericana.getNome())); // Dentro do corpo vai o valor do nome da cozinha,
                                                                           // que esta armazenado na variavel de instancia de cozinha, cozinhaAmericana
        }


    @Test
    public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente() {

        RestAssured.given() // Dado que
                .pathParam("cozinhaId", COZINHA_ID_INEXISTENTE) // o parametro de caminho, que é cozinhaId, nesse caso o valor de COZINHA_ID_INEXISTENTE = 100
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

        Cozinha cozinhaTailandesa = new Cozinha();
        cozinhaTailandesa.setNome("Tailandesa");
        cozinhaRepository.save(cozinhaTailandesa);

        cozinhaAmericana = new Cozinha();
        cozinhaAmericana.setNome("Americana");
        cozinhaRepository.save(cozinhaAmericana);

        quantidadeCozinhasCadastradas = (int) cozinhaRepository.count(); // Armazena na variavel a quantidade de novas cozinhas criadas

    }

}
