package pe.com.andinadistribuidora.service;

import java.util.List;
import pe.com.andinadistribuidora.api.request.RolRequestDto;
import pe.com.andinadistribuidora.api.response.RolResponseDto;

public interface RolService {
    
    RolResponseDto crear(RolRequestDto request);
    
    RolResponseDto actualizar(Integer idRol, RolRequestDto request);
    
    void eliminar(Integer idRol);
    
    RolResponseDto obtener(Integer idRol);
    
    List<RolResponseDto> listar();
}
