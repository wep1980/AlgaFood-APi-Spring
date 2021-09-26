package br.com.wepdev.domain.repository;

import br.com.wepdev.domain.model.Usuario;
import br.com.wepdev.infrastructure.repository.CustomJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long> {


    Optional<Usuario> findByEmail(String email);


	
}
