package pe.com.andinadistribuidora.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.InventarioMovimientoRequestDto;
import pe.com.andinadistribuidora.api.response.InventarioMovimientoResponseDto;
import pe.com.andinadistribuidora.entity.Inventario;
import pe.com.andinadistribuidora.entity.Inventario.InventarioId;
import pe.com.andinadistribuidora.entity.InventarioMovimiento;
import pe.com.andinadistribuidora.entity.InventarioMovimiento.TipoMovimiento;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.mapper.InventarioMovimientoMapper;
import pe.com.andinadistribuidora.repository.AlmacenRepository;
import pe.com.andinadistribuidora.repository.InventarioMovimientoRepository;
import pe.com.andinadistribuidora.repository.InventarioRepository;
import pe.com.andinadistribuidora.repository.ProductoRepository;
import pe.com.andinadistribuidora.service.InventarioMovimientoService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventarioMovimientoServiceImpl implements InventarioMovimientoService {

    private final InventarioMovimientoRepository movimientoRepo;
    private final InventarioRepository inventarioRepo;
    private final AlmacenRepository almacenRepo;
    private final ProductoRepository productoRepo;
    private final InventarioMovimientoMapper mapper;

    @Override
    public InventarioMovimientoResponseDto crear(InventarioMovimientoRequestDto req) {
        log.info("Creando movimiento de inventario: tipo={}, almacen={}, producto={}", 
            req.getTipoMovimiento(), req.getAlmacenId(), req.getProductoId());

        // Validaciones básicas
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

        // Validar tipo de movimiento
        TipoMovimiento tipo;
        try {
            tipo = TipoMovimiento.valueOf(req.getTipoMovimiento().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Tipo de movimiento inválido: " + req.getTipoMovimiento());
        }

        // Validar y actualizar el inventario según el tipo de movimiento
        InventarioId inventarioId = new InventarioId(req.getAlmacenId(), req.getProductoId());
        Inventario inventario = inventarioRepo.findById(inventarioId)
                .orElseThrow(() -> new BusinessException(
                    "No existe inventario para este almacén y producto. Debe crearlo primero."));

        // Calcular nueva cantidad según tipo de movimiento
        java.math.BigDecimal cantidadActual = inventario.getCantidad();
        java.math.BigDecimal cantidadMovimiento = req.getCantidad();
        java.math.BigDecimal nuevaCantidad;

        switch (tipo) {
            case ENTRADA:
                nuevaCantidad = cantidadActual.add(cantidadMovimiento);
                break;
            case SALIDA:
                nuevaCantidad = cantidadActual.subtract(cantidadMovimiento);
                if (nuevaCantidad.compareTo(java.math.BigDecimal.ZERO) < 0) {
                    throw new BusinessException("Stock insuficiente. Disponible: " + cantidadActual);
                }
                break;
            case AJUSTE:
                // El ajuste establece directamente la cantidad
                nuevaCantidad = cantidadMovimiento;
                break;
            default:
                throw new BusinessException("Tipo de movimiento no soportado: " + tipo);
        }

        // Actualizar inventario
        inventario.setCantidad(nuevaCantidad);
        inventario.setUltimaActualizacion(LocalDateTime.now());
        inventarioRepo.save(inventario);

        // Crear movimiento
        InventarioMovimiento entity = mapper.toEntity(req);
        if (entity.getFechaMovimiento() == null) {
            entity.setFechaMovimiento(LocalDateTime.now());
        }
        entity.setUltimaActualizacion(LocalDateTime.now());

        InventarioMovimiento saved = movimientoRepo.save(entity);
        log.debug("Movimiento creado id={}, nueva cantidad en inventario={}", saved.getId(), nuevaCantidad);
        return mapper.toResponseDto(saved);
    }

    @Override
    public InventarioMovimientoResponseDto actualizar(Long id, InventarioMovimientoRequestDto req) {
        log.info("Actualizando movimiento de inventario id={}", id);

        InventarioMovimiento actual = movimientoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimiento no encontrado: " + id));

        // IMPORTANTE: Actualizar movimientos puede ser riesgoso porque afecta el inventario
        // Se recomienda solo permitir actualizar campos como referencia, usuario, etc.
        // NO permitir cambiar cantidad, tipo de movimiento, almacén o producto
        
        log.warn("Actualizando movimiento id={} - Solo campos de referencia permitidos", id);

        // Solo actualizar campos seguros
        if (req.getReferencia() != null) {
            actual.setReferencia(req.getReferencia().trim());
        }
        if (req.getUsuario() != null) {
            actual.setUsuario(req.getUsuario().trim());
        }
        if (req.getCostoUnitario() != null) {
            actual.setCostoUnitario(req.getCostoUnitario());
        }

        actual.setUltimaActualizacion(LocalDateTime.now());

        InventarioMovimiento saved = movimientoRepo.save(actual);
        log.debug("Movimiento actualizado id={}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando movimiento de inventario id={}", id);

        InventarioMovimiento movimiento = movimientoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimiento no encontrado: " + id));

        // IMPORTANTE: Eliminar movimientos es MUY riesgoso
        // Se debe revertir el efecto en el inventario
        log.warn("Eliminando movimiento id={} - Revirtiendo efecto en inventario", id);

        InventarioId inventarioId = new InventarioId(movimiento.getAlmacenId(), movimiento.getProductoId());
        Inventario inventario = inventarioRepo.findById(inventarioId)
                .orElseThrow(() -> new BusinessException("Inventario no encontrado para revertir movimiento"));

        // Revertir el movimiento
        java.math.BigDecimal cantidadActual = inventario.getCantidad();
        java.math.BigDecimal cantidadMovimiento = movimiento.getCantidad();
        java.math.BigDecimal nuevaCantidad;

        switch (movimiento.getTipoMovimiento()) {
            case ENTRADA:
                // Revertir entrada = restar cantidad
                nuevaCantidad = cantidadActual.subtract(cantidadMovimiento);
                if (nuevaCantidad.compareTo(java.math.BigDecimal.ZERO) < 0) {
                    throw new BusinessException("No se puede eliminar: causaría stock negativo");
                }
                break;
            case SALIDA:
                // Revertir salida = sumar cantidad
                nuevaCantidad = cantidadActual.add(cantidadMovimiento);
                break;
            case AJUSTE:
                throw new BusinessException("No se pueden eliminar ajustes de inventario");
            default:
                throw new BusinessException("Tipo de movimiento no soportado");
        }

        inventario.setCantidad(nuevaCantidad);
        inventario.setUltimaActualizacion(LocalDateTime.now());
        inventarioRepo.save(inventario);

        movimientoRepo.delete(movimiento);
        log.debug("Movimiento eliminado id={}, cantidad revertida en inventario", id);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioMovimientoResponseDto obtener(Long id) {
        log.info("Obteniendo movimiento de inventario id={}", id);

        InventarioMovimiento movimiento = movimientoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimiento no encontrado: " + id));

        return mapper.toResponseDto(movimiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioMovimientoResponseDto> listar() {
        log.info("Listando todos los movimientos de inventario");

        return movimientoRepo.findAll().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioMovimientoResponseDto> listarPorAlmacen(Integer almacenId) {
        log.info("Listando movimientos por almacén: {}", almacenId);

        if (!almacenRepo.existsById(almacenId)) {
            throw new NotFoundException("Almacén no encontrado: " + almacenId);
        }

        return movimientoRepo.findByAlmacenId(almacenId).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioMovimientoResponseDto> listarPorProducto(Integer productoId) {
        log.info("Listando movimientos por producto: {}", productoId);

        if (!productoRepo.existsById(productoId)) {
            throw new NotFoundException("Producto no encontrado: " + productoId);
        }

        return movimientoRepo.findByProductoId(productoId).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioMovimientoResponseDto> listarPorTipo(String tipoMovimiento) {
        log.info("Listando movimientos por tipo: {}", tipoMovimiento);

        TipoMovimiento tipo;
        try {
            tipo = TipoMovimiento.valueOf(tipoMovimiento.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Tipo de movimiento inválido: " + tipoMovimiento);
        }

        return movimientoRepo.findByTipoMovimiento(tipo).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioMovimientoResponseDto> listarPorFechas(LocalDateTime inicio, LocalDateTime fin) {
        log.info("Listando movimientos entre {} y {}", inicio, fin);

        if (inicio.isAfter(fin)) {
            throw new BusinessException("La fecha de inicio debe ser anterior a la fecha de fin");
        }

        return movimientoRepo.findByFechaMovimientoBetween(inicio, fin).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioMovimientoResponseDto> listarUltimosMovimientos() {
        log.info("Listando últimos 10 movimientos");

        return movimientoRepo.findTop10ByOrderByFechaMovimientoDesc().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
}