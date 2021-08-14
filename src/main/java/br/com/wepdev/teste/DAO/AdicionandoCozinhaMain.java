package br.com.wepdev.teste.DAO;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import br.com.wepdev.TreinamentoAlgaFoodApiApplication;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;

public class AdicionandoCozinhaMain {

	public static void main(String[] args) {
	
		/**
		 * Iniciando uma aplicacao Spring nao web.
		 * TreinamentoAlgaFoodApiApplication -> utilizando dentro do builder a propria classe que estarta a aplicacao WEB
		 * web(WebApplicationType.NONE) -> Tipo de aplicacao web, nenhuma!
		 * PARA RODAR A APLICACAO DEVE SER PELO RUN AS -> JAVA APPLICATION
		 */
        ApplicationContext context = new SpringApplicationBuilder(TreinamentoAlgaFoodApiApplication.class)
        		.web(WebApplicationType.NONE).run(args);
        
       CozinhaRepository cadastroCozinha = context.getBean(CozinhaRepository.class); // Pegando um bean do tipo cadastro cozinha
   
	   Cozinha c1 = new Cozinha();
	   c1.setNome("Japonesa");
	   
	   Cozinha c2 = new Cozinha();
	   c2.setNome("Mexicana");
	 
	   cadastroCozinha.salvarOuAtualizar(c1);
	   
	   c2 = cadastroCozinha.salvarOuAtualizar(c2);
	   System.out.printf("%d - %s\n", c2.getId(), c2.getNome()); // %d pega o valor Long do getId , %s pega o valor String do getNome -- \n quebra a linha 
	}

}
