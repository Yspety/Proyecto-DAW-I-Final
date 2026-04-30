package pe.com.andinadistribuidora.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.UnidadRequestDto;
import pe.com.andinadistribuidora.api.response.UnidadResponseDto;
import pe.com.andinadistribuidora.entity.Unidad;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.mapper.UnidadMapper;
import pe.com.andinadistribuidora.repository.UnidadRepository;
import pe.com.andinadistribuidora.service.UnidadService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UnidadServiceImpl implements UnidadService {

    private final UnidadRepository unidadRepo;
    private final UnidadMapper mapper;

    @Override
    public UnidadResponseDto crear(UnidadRequestDto req) {
        log.info("Creando unidad: codigo='{}'", req.getCodigo());

        String codigo = req.getCodigo().trim().toUpperCase();

        if (unidadRepo.findByCodigo(codigo).isPresent()) {
            throw new BusinessException("El código de unidad ya existe: " + codigo);
        }

        Unidad entity = mapper.toEntity(req);
        entity.setCodigo(codigo);
        entity.setNombre(req.getNombre().trim());

        Unidad saved = unidadRepo.save(entity);
        log.debug("Unidad creada id={}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public UnidadResponseDto actualizar(Integer id, UnidadRequestDto req) {
        log.info("Actualizando unidad id={}", id);

        Unidad actual = unidadRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidad no encontrada: " + id));

        if (req.getCodigo() != null) {
            String nuevoCodigo = req.getCodigo().trim().toUpperCase();
            if (!nuevoCodigo.equalsIgnoreCase(actual.getCodigo())
                    && unidadRepo.existsByCodigoAndIdNot(nuevoCodigo, id)) {
                throw new BusinessException("El código de unidad ya existe: " + nuevoCodigo);
            }
        }

        mapper.updateEntityFromDto(req, actual);

        if (actual.getCodigo() != null) {
            actual.setCodigo(actual.getCodigo().trim().toUpperCase());
        }
        if (actual.getNombre() != null) {
            actual.setNombre(actual.getNombre().trim());
        }

        Unidad saved = unidadRepo.save(actual);
        log.debug("Unidad actualizada id={}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer id) {
        log.info("Eliminando unidad id={}", id);
        
        Unidad unidad = unidadRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidad no encontrada: " + id));

        // Verificar si hay productos vinculados (descomentar cuando tengas ProductoRepository)
        // long vinculados = productoRepo.countByUnidadId(id);
        // if (vinculados > 0) {
        //     throw new BusinessException("No se puede eliminar: tiene " + vinculados + " producto(s) asociado(s).");
        // }

        unidadRepo.delete(unidad);
        log.debug("Unidad eliminada id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public UnidadResponseDto obtener(Integer id) {
        log.info("Obteniendo unidad id={}", id);
        
        Unidad unidad = unidadRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidad no encontrada: " + id));
        
        return mapper.toResponseDto(unidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadResponseDto> listar() {
        log.info("Listando todas las unidades");
        
        return unidadRepo.findAll().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
}