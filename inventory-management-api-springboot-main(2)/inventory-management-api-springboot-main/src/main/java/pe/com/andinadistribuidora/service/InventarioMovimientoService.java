package pe.com.andinadistribuidora.service;

import java.time.LocalDateTime;
import java.util.List;
import pe.com.andinadistribuidora.api.request.InventarioMovimientoRequestDto;
import pe.com.andinadistribuidora.api.response.InventarioMovimientoResponseDto;

public interface InventarioMovimientoService {
    
    InventarioMovimientoResponseDto crear(InventarioMovimientoRequestDto request);
    
    InventarioMovimientoResponseDto actualizar(Long id, InventarioMovimientoRequestDto request);
    
    void eliminar(Long id);
    
    InventarioMovimientoResponseDto obtener(Long id);
    
    List<InventarioMovimientoResponseDto> listar();
    
    List<InventarioMovimientoResponseDto> listarPorAlmacen(Integer almacenId);
    
    List<InventarioMovimientoResponseDto> listarPorProducto(Integer productoId);
    
    List<InventarioMovimientoResponseDto> listarPorTipo(String tipoMovimiento);
    
    List<InventarioMovimientoResponseDto> listarPorFechas(LocalDateTime inicio, LocalDateTime fin);
    
    List<InventarioMovimientoResponseDto> listarUltimosMovimientos();
}