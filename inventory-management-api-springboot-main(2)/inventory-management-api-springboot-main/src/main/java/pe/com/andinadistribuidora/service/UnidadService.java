package pe.com.andinadistribuidora.service;

import java.util.List;

import pe.com.andinadistribuidora.api.request.UnidadRequestDto;
import pe.com.andinadistribuidora.api.response.UnidadResponseDto;


public interface UnidadService {
    
    UnidadResponseDto crear(UnidadRequestDto request);
    
    UnidadResponseDto actualizar(Integer id, UnidadRequestDto request);
    
    void eliminar(Integer id);
    
    UnidadResponseDto obtener(Integer id);
    
    List<UnidadResponseDto> listar();
}