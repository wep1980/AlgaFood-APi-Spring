package br.com.wepdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import br.com.wepdev.infrastructure.repository.CustomJpaRepositoryImpl;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class) // Configuracao do Repository base da aplicacao
public class TreinamentoAlgaFoodApiApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC")); // O Horario padrão da aplicação sera UTC
		SpringApplication.run(TreinamentoAlgaFoodApiApplication.class, args);
	}

}
