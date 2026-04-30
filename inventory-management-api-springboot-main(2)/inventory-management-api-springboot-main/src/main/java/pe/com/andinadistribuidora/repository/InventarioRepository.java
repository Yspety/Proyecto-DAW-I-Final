package pe.com.andinadistribuidora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.andinadistribuidora.entity.Inventario;
import pe.com.andinadistribuidora.entity.Inventario.InventarioId;

public interface InventarioRepository extends JpaRepository<Inventario, InventarioId> {
    
    // Buscar por almacén
    List<Inventario> findByIdAlmacenId(@Param("almacenId") Integer almacenId);
    
    // Buscar por producto
    List<Inventario> findByIdProductoId(@Param("productoId") Integer productoId);
    
    // Productos con stock bajo (cantidad < stock_min)
    @Query("SELECT i FROM Inventario i WHERE i.cantidad < i.stockMin")
    List<Inventario> findProductosConStockBajo();
    
    // Productos con stock crítico (cantidad <= stock_min * 0.5)
    @Query("SELECT i FROM Inventario i WHERE i.cantidad <= (i.stockMin * 0.5)")
    List<Inventario> findProductosConStockCritico();
    
    // Verificar si existe inventario para un almacén-producto
    boolean existsByIdAlmacenIdAndIdProductoId(Integer almacenId, Integer productoId);
}