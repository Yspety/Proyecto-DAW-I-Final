package pe.com.andinadistribuidora.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.CategoriaRequestDto;
import pe.com.andinadistribuidora.api.response.CategoriaResponseDto;
import pe.com.andinadistribuidora.entity.Categoria;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.mapper.CategoriaMapper;
import pe.com.andinadistribuidora.repository.CategoriaRepository;
import pe.com.andinadistribuidora.service.CategoriaService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepo;
    private final CategoriaMapper mapper;

    @Override
    public CategoriaResponseDto crear(CategoriaRequestDto req) {
        log.info("Creando categoría '{}'", req.getNombre());

        // Validar que el nombre no esté vacío
        if (req.getNombre() == null || req.getNombre().trim().isEmpty()) {
            throw new BusinessException("El nombre de la categoría es obligatorio");
        }

        // Validar que no exista otra categoría con el mismo nombre
        String nombreNormalizado = req.getNombre().trim().toUpperCase();
        if (categoriaRepo.findByNombre(nombreNormalizado).isPresent()) {
            throw new BusinessException("Ya existe una categoría con el nombre: " + req.getNombre());
        }

        // Mapear DTO -> Entity
        Categoria entity = mapper.toEntity(req);
        entity.setNombre(nombreNormalizado);
        entity.setUltimaActualizacion(LocalDateTime.now());
        
        // Si no se especifica activo, por defecto es true
        if (entity.getActivo() == null) {
            entity.setActivo(true);
        }

        Categoria saved = categoriaRepo.save(entity);
        log.debug("Categoría creada id='{}'", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public CategoriaResponseDto actualizar(Integer id, CategoriaRequestDto req) {
        log.info("Actualizando categoría id='{}'", id);

        Categoria actual = categoriaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + id));

        // Validar que el nombre no esté en uso por otra categoría
        if (req.getNombre() != null && !req.getNombre().trim().isEmpty()) {
            String nombreNormalizado = req.getNombre().trim().toUpperCase();
            
            // Verificar si existe otra categoría con ese nombre (excluyendo la actual)
            if (categoriaRepo.existsByNombreAndIdNot(nombreNormalizado, id)) {
                throw new BusinessException("Ya existe otra categoría con el nombre: " + req.getNombre());
            }
            
            actual.setNombre(nombreNormalizado);
        }

        // Actualizar otros campos
        if (req.getDescripcion() != null) {
            actual.setDescripcion(req.getDescripcion().trim());
        }
        
        if (req.getActivo() != null) {
            actual.setActivo(req.getActivo());
        }

        actual.setUltimaActualizacion(LocalDateTime.now());

        Categoria saved = categoriaRepo.save(actual);
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer id) {
        log.info("Eliminando categoría id='{}'", id);
        Categoria c = categoriaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + id));
        
        // Aquí podrías agregar validación para no eliminar si tiene productos asociados
        // Por ejemplo: if (productoRepo.countByCategoriaId(id) > 0) throw new BusinessException(...)
        
        categoriaRepo.delete(c);
        log.debug("Categoría eliminada id='{}'", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDto obtener(Integer id) {
        Categoria c = categoriaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada: " + id));
        return mapper.toResponseDto(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> listar() {
        return categoriaRepo.findAll().stream()
                .map(mapper::toResponseDto)
                .toList();
    }
    
    // Método adicional para listar solo activas (opcional)
    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> listarActivas() {
        return categoriaRepo.findByActivoTrue().stream()
                .map(mapper::toResponseDto)
                .toList();
    }
}