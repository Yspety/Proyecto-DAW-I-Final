package pe.com.andinadistribuidora.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.RolRequestDto;
import pe.com.andinadistribuidora.api.response.RolResponseDto;
import pe.com.andinadistribuidora.entity.Rol;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.mapper.RolMapper;
import pe.com.andinadistribuidora.repository.RolRepository;
import pe.com.andinadistribuidora.repository.UsuarioRepository;
import pe.com.andinadistribuidora.service.RolService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepo;
    private final UsuarioRepository usuarioRepo;
    private final RolMapper mapper;

    @Override
    public RolResponseDto crear(RolRequestDto req) {
        log.info("Creando rol: descripcion='{}'", req.getDescripcion());

        // Normalización
        String descripcion = req.getDescripcion().trim().toUpperCase();

        // Validación de unicidad
        if (rolRepo.existsByDescripcion(descripcion)) {
            throw new BusinessException("La descripción del rol ya existe: " + descripcion);
        }

        // Mapear y persistir
        Rol entity = mapper.toEntity(req);
        entity.setDescripcion(descripcion);

        Rol saved = rolRepo.save(entity);
        log.debug("Rol creado id={}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public RolResponseDto actualizar(Integer idRol, RolRequestDto req) {
        log.info("Actualizando rol id={}", idRol);

        Rol actual = rolRepo.findById(idRol)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + idRol));

        // Validación de unicidad si cambia la descripción
        if (req.getDescripcion() != null) {
            String nueva = req.getDescripcion().trim().toUpperCase();
            if (!nueva.equalsIgnoreCase(actual.getDescripcion())
                    && rolRepo.existsByDescripcion(nueva)) {
                throw new BusinessException("La descripción del rol ya existe: " + nueva);
            }
        }

        // Actualización parcial con MapStruct (ignora nulos)
        mapper.updateEntityFromDto(req, actual);

        // Normalización final
        if (actual.getDescripcion() != null) {
            actual.setDescripcion(actual.getDescripcion().trim().toUpperCase());
        }

        Rol saved = rolRepo.save(actual);
        log.debug("Rol actualizado id={}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer idRol) {
        log.info("Eliminando rol id={}", idRol);
        
        Rol rol = rolRepo.findById(idRol)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + idRol));

        // Validar que no haya usuarios asociados
        long usuariosVinculados = usuarioRepo.countByRolId(idRol);
        if (usuariosVinculados > 0) {
            throw new BusinessException("No se puede eliminar: tiene " + usuariosVinculados + " usuario(s) asociado(s).");
        }

        rolRepo.delete(rol);
        log.debug("Rol eliminado id={}", idRol);
    }

    @Override
    @Transactional(readOnly = true)
    public RolResponseDto obtener(Integer idRol) {
        log.info("Obteniendo rol id={}", idRol);
        
        Rol rol = rolRepo.findById(idRol)
                .orElseThrow(() -> new NotFoundException("Rol no encontrado: " + idRol));
        
        return mapper.toResponseDto(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolResponseDto> listar() {
        log.info("Listando todos los roles");
        
        return rolRepo.findAll().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
