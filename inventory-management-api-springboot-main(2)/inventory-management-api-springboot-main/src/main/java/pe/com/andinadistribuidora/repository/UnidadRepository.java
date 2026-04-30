package pe.com.andinadistribuidora.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import pe.com.andinadistribuidora.entity.Unidad;

public interface UnidadRepository extends JpaRepository<Unidad, Integer> {
    
    Optional<Unidad> findByCodigo(@Param("codigo") String codigo);
    
    boolean existsByCodigoAndIdNot(@Param("codigo") String codigo, @Param("id") Integer id);
    
    Optional<Unidad> findByEsBaseTrue();
}