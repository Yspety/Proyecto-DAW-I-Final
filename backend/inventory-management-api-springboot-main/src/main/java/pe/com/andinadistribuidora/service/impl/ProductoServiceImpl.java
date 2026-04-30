package pe.com.andinadistribuidora.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.ProductoRequestDto;
import pe.com.andinadistribuidora.api.response.ProductoResponseDto;
import pe.com.andinadistribuidora.entity.Producto;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.mapper.ProductoMapper;
import pe.com.andinadistribuidora.repository.CategoriaRepository;
import pe.com.andinadistribuidora.repository.ProductoRepository;
import pe.com.andinadistribuidora.repository.UnidadRepository;
import pe.com.andinadistribuidora.service.ProductoService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepo;
    private final CategoriaRepository categoriaRepo;
    private final UnidadRepository unidadRepo;
    private final ProductoMapper mapper;

    @Override
    public ProductoResponseDto crear(ProductoRequestDto req) {
        log.info("Creando producto: sku='{}', nombre='{}'", req.getSku(), req.getNombre());

        // Normalizaciones
        String sku = req.getSku().trim().toUpperCase();
        String nombre = req.getNombre().trim();

        // Validación de unicidad del SKU
        if (productoRepo.findBySku(sku).isPresent()) {
            throw new BusinessException("El SKU ya está registrado: " + sku);
        }

        // Validar que la categoría existe
        if (req.getCategoriaId() != null && !categoriaRepo.existsById(req.getCategoriaId())) {
            throw new NotFoundException("Categoría no encontrada: " + req.getCategoriaId());
        }

        // Validar que la unidad existe
        if (req.getUnidadId() != null && !unidadRepo.existsById(req.getUnidadId())) {
            throw new NotFoundException("Unidad no encontrada: " + req.getUnidadId());
        }

        // Mapear y persistir
        Producto entity = mapper.toEntity(req);
        entity.setSku(sku);
        entity.setNombre(nombre);
        entity.setCreadoEn(LocalDateTime.now());

        Producto saved = productoRepo.save(entity);
        log.debug("Producto creado id={}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public ProductoResponseDto actualizar(Integer id, ProductoRequestDto req) {
        log.info("Actualizando producto id={}", id);

        Producto actual = productoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));

        // Validación de unicidad del SKU si cambia
        if (req.getSku() != null) {
            String nuevoSku = req.getSku().trim().toUpperCase();
            if (!nuevoSku.equalsIgnoreCase(actual.getSku())
                    && productoRepo.existsBySkuAndIdNot(nuevoSku, id)) {
                throw new BusinessException("El SKU ya está registrado: " + nuevoSku);
            }
        }

        // Validar que la categoría existe si se proporciona
        if (req.getCategoriaId() != null && !categoriaRepo.existsById(req.getCategoriaId())) {
            throw new NotFoundException("Categoría no encontrada: " + req.getCategoriaId());
        }

        // Validar que la unidad existe si se proporciona
        if (req.getUnidadId() != null && !unidadRepo.existsById(req.getUnidadId())) {
            throw new NotFoundException("Unidad no encontrada: " + req.getUnidadId());
        }

        // Actualización parcial con MapStruct (ignora nulos)
        mapper.updateEntityFromDto(req, actual);

        // Normalizaciones finales
        if (actual.getSku() != null) actual.setSku(actual.getSku().trim().toUpperCase());
        if (actual.getNombre() != null) actual.setNombre(actual.getNombre().trim());

        Producto saved = productoRepo.save(actual);
        log.debug("Producto actualizado id={}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Override
    public void eliminar(Integer id) {
        log.info("Eliminando producto id={}", id);
        
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));

        // Aquí podrías validar si tiene inventario o movimientos antes de eliminar
        // Por ejemplo:
        // long movimientos = inventarioMovimientoRepo.countByProductoId(id);
        // if (movimientos > 0) {
        //     throw new BusinessException("No se puede eliminar: tiene movimientos de inventario asociados");
        // }

        productoRepo.delete(producto);
        log.debug("Producto eliminado id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto obtener(Integer id) {
        log.info("Obteniendo producto id={}", id);
        
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));
        
        return mapper.toResponseDto(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> listar() {
        log.info("Listando todos los productos");
        
        return productoRepo.findAll().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> listarActivos() {
        log.info("Listando productos activos");
        
        return productoRepo.findByActivoTrue().stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> listarPorCategoria(Integer categoriaId) {
        log.info("Listando productos por categoría id={}", categoriaId);
        
        // Validar que la categoría existe
        if (!categoriaRepo.existsById(categoriaId)) {
            throw new NotFoundException("Categoría no encontrada: " + categoriaId);
        }
        
        return productoRepo.findByCategoriaId(categoriaId).stream()
                .map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }
}