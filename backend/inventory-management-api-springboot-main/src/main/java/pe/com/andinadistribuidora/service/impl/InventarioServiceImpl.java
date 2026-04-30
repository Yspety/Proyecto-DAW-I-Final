package pe.com.andinadistribuidora.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.InventarioRequestDto;
import pe.com.andinadistribuidora.api.response.InventarioResponseDto;
import pe.com.andinadistribuidora.entity.Inventario;
import pe.com.andinadistribuidora.entity.Inventario.InventarioId;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.mapper.InventarioMapper;
import pe.com.andinadistribuidora.repository.AlmacenRepository;
import pe.com.andinadistribuidora.repository.InventarioRepository;
import pe.com.andinadistribuidora.repository.ProductoRepository;
import pe.com.andinadistribuidora.service.InventarioService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepo;
    private final AlmacenRepository almacenRepo;
    private final ProductoRepository productoRepo;
    private final InventarioMapper mapper;

    @Override
    public InventarioResponseDto crear(InventarioRequestDto req) {
        log.info("Creando inventario: almacen={}, producto={}", req.getAlmacenId(), req.getProductoId());

        // Validar que almacén y producto no sean nulos
        if (req.getAlmacenId() == null || req.getProductoId() == null) {
            throw new BusinessException("El almacén y el producto son obligatorios");
        }

        // Validar que el almacén existe
        if (!almacenRepo.existsById(req.getAlmacenId())) {
            throw new NotFoundException("Almacén no encontrado: " + req.getAlmacenId());
        }

        // Validar que el producto existe
        if (!productoRepo.existsById(req.getProductoId())) {
            throw new NotFoundException("Producto no encontrado: " + req.getProductoId());
        }

        // Validar que no existe ya este inventario
        InventarioId id = new InventarioId(req.getAlmacenId(), req.getProductoId());
        if (inventarioRepo.existsById(id)) {
            throw new BusinessException("Ya existe inventario para este almacén y producto");
        }

        // Validar stock_max > stock_min si se proporciona
        if (req.getStockMax() != null && req.getStockMax().compareTo(req.getStockMin()) < 0) {
            throw new BusinessException("El stock máximo debe ser mayor o igual al stock mínimo");
        }

        // Mapear y persistir
        Inventario entity = mapper.toEntity(req);
        entity.setUltimaActualizacion(LocalDateTime.now());

        Inventario saved = inventarioRepo.save(entity);
        log.debug("Inventario creado: almacen={}, producto={}", saved.getId().getAlmacenId(), saved.getId().getProductoId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public InventarioResponseDto actualizar(Integer almacenId, Integer productoId, InventarioRequestDto req) {
        log.info("Actualizando inventario: almacen={}, producto={}", almacenId, productoId);

        InventarioId id = new InventarioId(almacenId, productoId);
        Inventario actual = inventarioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    String.format("Inventario no encontrado: almacén=%d, producto=%d", almacenId, productoId)));

        // Validar stock_max > stock_min si se actualiza
        if (req.getStockMax() != null) {
            java.math.BigDecimal nuevoStockMin = req.getStockMin() != null ? req.getStockMin() : actual.getStockMin();
            if (req.getStockMax().compareTo(nuevoStockMin) < 0) {
                throw new BusinessException("El stock máximo debe ser mayor o igual al stock mínimo");
            }
        }

        // Actualización parcial con MapStruct (ignora nulos)
        mapper.updateEntityFromDto(req, actual);
        actual.setUltimaActualizacion(LocalDateTime.now());

        Inventario saved = inventarioRepo.save(actual);
        log.debug("Inventario actualizado: almacen={}, producto={}", saved.getId().getAlmacenId(), saved.getId().getProductoId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer almacenId, Integer productoId) {
        log.info("Eliminando inventario: almacen={}, producto={}", almacenId, productoId);

        InventarioId id = new InventarioId(almacenId, productoId);
        Inventario inventario = inventarioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    String.format("Inventario no encontrado: almacén=%d, producto=%d", almacenId, productoId)));

        // Aquí podrías validar si hay movimientos antes de eliminar
        // Por ejemplo:
        // long movimientos = inventarioMovimientoRepo.countByAlmacenIdAndProductoId(almacenId, productoId);
        // if (movimientos > 0) {
        //     throw new BusinessException("No se puede eliminar: tiene movimientos asociados");
        // }

        inventarioRepo.delete(inventario);
        log.debug("Inventario eliminado: almacen={}, producto={}", almacenId, productoId);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioResponseDto obtener(Integer almacenId, Integer productoId) {
        log.info("Obteniendo inventario: almacen={}, producto={}", almacenId, productoId);

        InventarioId id = new InventarioId(almacenId, productoId);
        Inventario inventario = inventarioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    String.format("Inventario no encontrado: almacén=%d, producto=%d", almacenId, productoId)));

        return mapper.toResponseDto(inventario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponseDto> listar() {
        log.info("Listando todo el inventario");

        return inventarioRepo.findAll().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponseDto> listarPorAlmacen(Integer almacenId) {
        log.info("Listando inventario por almacén: {}", almacenId);

        // Validar que el almacén existe
        if (!almacenRepo.existsById(almacenId)) {
            throw new NotFoundException("Almacén no encontrado: " + almacenId);
        }

        return inventarioRepo.findByIdAlmacenId(almacenId).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponseDto> listarPorProducto(Integer productoId) {
        log.info("Listando inventario por producto: {}", productoId);

        // Validar que el producto existe
        if (!productoRepo.existsById(productoId)) {
            throw new NotFoundException("Producto no encontrado: " + productoId);
        }

        return inventarioRepo.findByIdProductoId(productoId).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponseDto> listarStockBajo() {
        log.info("Listando productos con stock bajo");

        return inventarioRepo.findProductosConStockBajo().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioResponseDto> listarStockCritico() {
        log.info("Listando productos con stock crítico");

        return inventarioRepo.findProductosConStockCritico().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
}