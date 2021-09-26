package br.com.wepdev.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repositorio generico
 * <T , ID> Entidade e Id
 * @author Waldir
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean // O spring nao instancia esse repository
public interface CustomJpaRepository<T , ID> extends JpaRepository<T, ID>{
	
	
	Optional<T> buscarPrimeiro();

	// Metodo que retira um objeto do gerenciamento do JPA, evitando assim um sincronismo antes da ora
	void detach(T entity);

}
