package br.com.wepdev.teste.DAO;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import br.com.wepdev.TreinamentoAlgaFoodApiApplication;
import br.com.wepdev.domain.model.Cozinha;
import br.com.wepdev.domain.repository.CozinhaRepository;

public class BuscaPorIdCozinhaMain {

	public static void main(String[] args) {
	
		/**
		 * Iniciando uma aplicacao Spring nao web.
		 * TreinamentoAlgaFoodApiApplication -> utilizando dentro do builder a propria classe que estarta a aplicacao WEB
		 * web(WebApplicationType.NONE) -> Tipo de aplicacao web, nenhuma!
		 * PARA RODAR A APLICACAO DEVE SER PELO RUN AS -> JAVA APPLICATION
		 */
        ApplicationContext context = new SpringApplicationBuilder(TreinamentoAlgaFoodApiApplication.class)
        		.web(WebApplicationType.NONE).run(args);
        
        CozinhaRepository cozinha = context.getBean(CozinhaRepository.class); // Pegando um bean do tipo cadastro cozinha
   
       Cozinha cozi = cozinha.porId(1L);
       System.out.println(cozi.getNome());
       
	}

}
