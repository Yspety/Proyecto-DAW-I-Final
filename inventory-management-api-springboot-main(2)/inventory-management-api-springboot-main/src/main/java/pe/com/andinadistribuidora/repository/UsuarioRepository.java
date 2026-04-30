package pe.com.andinadistribuidora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import pe.com.andinadistribuidora.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // Buscar usuario por nombre de usuario (para login)
    Optional<Usuario> findByUsuario(@Param("usuario") String usuario);
    
    // Listar usuarios por rol
    List<Usuario> findByRolId(@Param("rolId") Integer rolId);
    
    // Contar usuarios por rol
    long countByRolId(Integer rolId);
    
    // Listar usuarios activos
    List<Usuario> findByActivo(@Param("activo") Boolean activo);
}
