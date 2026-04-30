package pe.com.andinadistribuidora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import pe.com.andinadistribuidora.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    List<Producto> findByCategoriaId(@Param("categoriaId") Integer categoriaId);
    
    List<Producto> findByActivoTrue();
    
    Optional<Producto> findBySku(@Param("sku") String sku);
    
    boolean existsBySkuAndIdNot(@Param("sku") String sku, @Param("id") Integer id);
    
    long countByCategoriaId(Integer categoriaId);
    
    long countByUnidadId(Integer unidadId);
}