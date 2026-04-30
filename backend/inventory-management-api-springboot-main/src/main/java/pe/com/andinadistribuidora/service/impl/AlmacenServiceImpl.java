package pe.com.andinadistribuidora.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.AlmacenRequestDto;
import pe.com.andinadistribuidora.api.response.AlmacenResponseDto;
import pe.com.andinadistribuidora.entity.Almacen;
import pe.com.andinadistribuidora.entity.Almacen.TipoAlmacen;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.mapper.AlmacenMapper;
import pe.com.andinadistribuidora.repository.AlmacenRepository;
import pe.com.andinadistribuidora.service.AlmacenService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AlmacenServiceImpl implements AlmacenService {

    private final AlmacenRepository almacenRepo;
    private final AlmacenMapper mapper;

    @Override
    public AlmacenResponseDto crear(AlmacenRequestDto req) {
        log.info("Creando almacén: nombre='{}'", req.getNombre());

        Almacen entity = mapper.toEntity(req);
        entity.setNombre(req.getNombre().trim());
        if (req.getDireccion() != null) {
            entity.setDireccion(req.getDireccion().trim());
        }

        Almacen saved = almacenRepo.save(entity);
        log.debug("Almacén creado id={}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public AlmacenResponseDto actualizar(Integer id, AlmacenRequestDto req) {
        log.info("Actualizando almacén id={}", id);

        Almacen actual = almacenRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Almacén no encontrado: " + id));

        mapper.updateEntityFromDto(req, actual);

        if (actual.getNombre() != null) {
            actual.setNombre(actual.getNombre().trim());
        }
        if (actual.getDireccion() != null) {
            actual.setDireccion(actual.getDireccion().trim());
        }

        Almacen saved = almacenRepo.save(actual);
        log.debug("Almacén actualizado id={}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer id) {
        log.info("Eliminando almacén id={}", id);
        
        Almacen almacen = almacenRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Almacén no encontrado: " + id));

        almacenRepo.delete(almacen);
        log.debug("Almacén eliminado id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public AlmacenResponseDto obtener(Integer id) {
        log.info("Obteniendo almacén id={}", id);
        
        Almacen almacen = almacenRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Almacén no encontrado: " + id));
        
        return mapper.toResponseDto(almacen);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlmacenResponseDto> listar() {
        log.info("Listando todos los almacenes");
        
        return almacenRepo.findAll().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlmacenResponseDto> listarActivos() {
        log.info("Listando almacenes activos");
        
        return almacenRepo.findByActivoTrue().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlmacenResponseDto> listarPorTipo(TipoAlmacen tipo) {
        log.info("Listando almacenes por tipo={}", tipo);
        
        return almacenRepo.findByTipo(tipo).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
}