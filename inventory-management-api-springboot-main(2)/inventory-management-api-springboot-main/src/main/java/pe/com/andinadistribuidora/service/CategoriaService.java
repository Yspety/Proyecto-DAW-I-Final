package pe.com.andinadistribuidora.service;

import java.util.List;
import pe.com.andinadistribuidora.api.request.CategoriaRequestDto;
import pe.com.andinadistribuidora.api.response.CategoriaResponseDto;

public interface CategoriaService {
    CategoriaResponseDto crear(CategoriaRequestDto request);
    CategoriaResponseDto actualizar(Integer id, CategoriaRequestDto request);
    void eliminar(Integer id);
    CategoriaResponseDto obtener(Integer id);
    List<CategoriaResponseDto> listar();
}