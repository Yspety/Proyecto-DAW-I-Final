package pe.com.andinadistribuidora.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.andinadistribuidora.entity.InventarioMovimiento;
import pe.com.andinadistribuidora.entity.InventarioMovimiento.TipoMovimiento;

public interface InventarioMovimientoRepository extends JpaRepository<InventarioMovimiento, Long> {
    
    // Movimientos por almacén
    List<InventarioMovimiento> findByAlmacenId(@Param("almacenId") Integer almacenId);
    
    // Movimientos por producto
    List<InventarioMovimiento> findByProductoId(@Param("productoId") Integer productoId);
    
    // Movimientos por tipo
    List<InventarioMovimiento> findByTipoMovimiento(@Param("tipoMovimiento") TipoMovimiento tipoMovimiento);
    
    // Movimientos por almacén y producto
    List<InventarioMovimiento> findByAlmacenIdAndProductoId(
        @Param("almacenId") Integer almacenId, 
        @Param("productoId") Integer productoId
    );
    
    // Movimientos por rango de fechas
    @Query("SELECT im FROM InventarioMovimiento im WHERE im.fechaMovimiento BETWEEN :inicio AND :fin ORDER BY im.fechaMovimiento DESC")
    List<InventarioMovimiento> findByFechaMovimientoBetween(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin
    );
    
    // Últimos movimientos (útil para dashboard)
    List<InventarioMovimiento> findTop10ByOrderByFechaMovimientoDesc();
    
    // Contar movimientos por producto (para validar antes de eliminar)
    long countByProductoId(Integer productoId);
}