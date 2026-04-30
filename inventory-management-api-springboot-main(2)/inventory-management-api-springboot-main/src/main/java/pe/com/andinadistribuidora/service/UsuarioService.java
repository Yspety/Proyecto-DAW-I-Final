package pe.com.andinadistribuidora.service;

import java.util.List;

import pe.com.andinadistribuidora.api.request.UsuarioRequestDto;
import pe.com.andinadistribuidora.api.response.UsuarioResponseDto;

public interface UsuarioService {
    
    UsuarioResponseDto crear(UsuarioRequestDto request);
    
    UsuarioResponseDto actualizar(Integer idUsuario, UsuarioRequestDto request);
    
    void eliminar(Integer idUsuario);
    
    UsuarioResponseDto obtener(Integer idUsuario);
    
    List<UsuarioResponseDto> listar();
    
    List<UsuarioResponseDto> listarPorRol(Integer idRol);
}