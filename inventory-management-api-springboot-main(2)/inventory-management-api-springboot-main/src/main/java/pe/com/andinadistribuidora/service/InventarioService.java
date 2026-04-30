package pe.com.andinadistribuidora.service;

import java.util.List;
import pe.com.andinadistribuidora.api.request.InventarioRequestDto;
import pe.com.andinadistribuidora.api.response.InventarioResponseDto;

public interface InventarioService {
    
    InventarioResponseDto crear(InventarioRequestDto request);
    
    InventarioResponseDto actualizar(Integer almacenId, Integer productoId, InventarioRequestDto request);
    
    void eliminar(Integer almacenId, Integer productoId);
    
    InventarioResponseDto obtener(Integer almacenId, Integer productoId);
    
    List<InventarioResponseDto> listar();
    
    List<InventarioResponseDto> listarPorAlmacen(Integer almacenId);
    
    List<InventarioResponseDto> listarPorProducto(Integer productoId);
    
    List<InventarioResponseDto> listarStockBajo();
    
    List<InventarioResponseDto> listarStockCritico();
}