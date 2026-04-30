package pe.com.andinadistribuidora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import pe.com.andinadistribuidora.entity.Almacen;
import pe.com.andinadistribuidora.entity.Almacen.TipoAlmacen;


public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
    
    List<Almacen> findByActivoTrue();
    
    List<Almacen> findByTipo(@Param("tipo") TipoAlmacen tipo);
    
    List<Almacen> findByActivoTrueAndTipo(@Param("tipo") TipoAlmacen tipo);
}