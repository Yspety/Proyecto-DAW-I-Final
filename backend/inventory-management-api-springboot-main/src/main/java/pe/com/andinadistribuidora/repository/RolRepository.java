package pe.com.andinadistribuidora.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.com.andinadistribuidora.entity.Rol;


public interface RolRepository extends JpaRepository<Rol, Integer> {
    
    // Buscar rol por descripción (útil para validaciones de unicidad)
    Optional<Rol> findByDescripcion(String descripcion);
    
    // Verificar si existe un rol con una descripción específica
    boolean existsByDescripcion(String descripcion);
    
    // Verificar si existe un rol con una descripción específica excluyendo un ID
    // (útil para validar unicidad al actualizar)
    boolean existsByDescripcionAndIdNot(String descripcion, Integer id);
}