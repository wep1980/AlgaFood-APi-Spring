package br.com.wepdev.teste.DAO;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import br.com.wepdev.TreinamentoAlgaFoodApiApplication;
import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.RestauranteRepository;

public class ConsultaRestauranteMain {

	public static void main(String[] args) {
	
		/**
		 * Iniciando uma aplicacao Spring nao web.
		 * TreinamentoAlgaFoodApiApplication -> utilizando dentro do builder a propria classe que estarta a aplicacao WEB
		 * web(WebApplicationType.NONE) -> Tipo de aplicacao web, nenhuma!
		 * PARA RODAR A APLICACAO DEVE SER PELO RUN AS -> JAVA APPLICATION
		 */
        ApplicationContext context = new SpringApplicationBuilder(TreinamentoAlgaFoodApiApplication.class)
        		.web(WebApplicationType.NONE).run(args);
        
        
       RestauranteRepository restaurantes = context.getBean(RestauranteRepository.class); // Pegando um bean do tipo cadastro cozinha
       List<Restaurante> todosRestaurantes = restaurantes.listar();
       
       //  Objeto - Variavel - List cozinhas
       for (Restaurante restaurante  :  todosRestaurantes) {
		   System.out.printf("%s - %f - %s\n", restaurante.getNome() , restaurante.getTaxaFrete() , restaurante.getCozinha().getNome());
	}
	}

}
