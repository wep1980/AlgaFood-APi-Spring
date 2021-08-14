package br.com.wepdev.teste.DAO;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import br.com.wepdev.TreinamentoAlgaFoodApiApplication;
import br.com.wepdev.domain.model.Restaurante;
import br.com.wepdev.domain.repository.RestauranteRepository;

public class AdicionandoRestauranteMain {

	public static void main(String[] args) {
	
		/**
		 * Iniciando uma aplicacao Spring nao web.
		 * TreinamentoAlgaFoodApiApplication -> utilizando dentro do builder a propria classe que estarta a aplicacao WEB
		 * web(WebApplicationType.NONE) -> Tipo de aplicacao web, nenhuma!
		 * PARA RODAR A APLICACAO DEVE SER PELO RUN AS -> JAVA APPLICATION
		 */
        ApplicationContext context = new SpringApplicationBuilder(TreinamentoAlgaFoodApiApplication.class)
        		.web(WebApplicationType.NONE).run(args);
        
       RestauranteRepository restauranteRespository = context.getBean(RestauranteRepository.class); // Pegando um bean do tipo cadastro cozinha
   
	   Restaurante r1 = new Restaurante();
	   r1.setNome("Zingara");
	   
	   Restaurante r2 = new Restaurante();
	   r2.setNome("Pavao azul");
	 
	   restauranteRespository.salvarOuAtualizar(r1);
	   
	   r2 = restauranteRespository.salvarOuAtualizar(r2);
	   System.out.printf("%d - %s\n", r2.getId(), r2.getNome()); // %d pega o valor Long do getId , %s pega o valor String do getNome -- \n quebra a linha 
	}

}
