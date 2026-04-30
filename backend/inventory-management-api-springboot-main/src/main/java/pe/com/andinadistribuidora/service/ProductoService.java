package pe.com.andinadistribuidora.service;

import java.util.List;

import pe.com.andinadistribuidora.api.request.ProductoRequestDto;
import pe.com.andinadistribuidora.api.response.ProductoResponseDto;

public interface ProductoService {
    
    ProductoResponseDto crear(ProductoRequestDto request);
    
    ProductoResponseDto actualizar(Integer id, ProductoRequestDto request);
    
    void eliminar(Integer id);
    
    ProductoResponseDto obtener(Integer id);
    
    List<ProductoResponseDto> listar();
    
    List<ProductoResponseDto> listarActivos();
    
    List<ProductoResponseDto> listarPorCategoria(Integer categoriaId);
}