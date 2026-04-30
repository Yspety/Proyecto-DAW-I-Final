package pe.com.andinadistribuidora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import pe.com.andinadistribuidora.entity.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
    // Buscar categorías activas
    List<Categoria> findByActivoTrue();
    
    // Buscar por nombre 
    Optional<Categoria> findByNombre(@Param("nombre") String nombre);
    
    // Verificar si existe una categoría con ese nombre (excluyendo un ID específico para updates)
    boolean existsByNombreAndIdNot(@Param("nombre") String nombre, @Param("id") Integer id);
}