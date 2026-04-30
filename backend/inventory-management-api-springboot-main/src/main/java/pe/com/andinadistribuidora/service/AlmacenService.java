package pe.com.andinadistribuidora.service;

import java.util.List;

import pe.com.andinadistribuidora.api.request.AlmacenRequestDto;
import pe.com.andinadistribuidora.api.response.AlmacenResponseDto;
import pe.com.andinadistribuidora.entity.Almacen.TipoAlmacen;


public interface AlmacenService {
    
    AlmacenResponseDto crear(AlmacenRequestDto request);
    
    AlmacenResponseDto actualizar(Integer id, AlmacenRequestDto request);
    
    void eliminar(Integer id);
    
    AlmacenResponseDto obtener(Integer id);
    
    List<AlmacenResponseDto> listar();
    
    List<AlmacenResponseDto> listarActivos();
    
    List<AlmacenResponseDto> listarPorTipo(TipoAlmacen tipo);
}