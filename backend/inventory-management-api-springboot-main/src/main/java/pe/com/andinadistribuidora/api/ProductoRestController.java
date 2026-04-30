package pe.com.andinadistribuidora.api;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.ProductoRequestDto;
import pe.com.andinadistribuidora.api.response.ProductoResponseDto;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.service.ProductoService;

@Slf4j
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoRestController {
    
    private final ProductoService productoService;    
   
    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        try {
            List<ProductoResponseDto> productos = productoService.listar();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Productos listados correctamente");
            response.put("data", productos);
            response.put("total", productos.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al listar productos: {}", e.getMessage());
            return buildErrorResponse("Error al listar productos: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Integer id) {
        try {
            ProductoResponseDto producto = productoService.obtener(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto encontrado");
            response.put("data", producto);
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Producto no encontrado: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al obtener producto {}: {}", id, e.getMessage());
            return buildErrorResponse("Error al obtener producto: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(
            @Validated @RequestBody ProductoRequestDto request) {
        try {
            ProductoResponseDto productoCreado = productoService.crear(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto creado exitosamente");
            response.put("data", productoCreado);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BusinessException e) {
            log.error("Error de negocio al crear producto: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            log.error("Recurso no encontrado al crear producto: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al crear producto: {}", e.getMessage(), e);
            return buildErrorResponse("Error al crear producto: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Integer id,
            @Validated @RequestBody ProductoRequestDto request) {
        try {
            ProductoResponseDto productoActualizado = productoService.actualizar(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto actualizado exitosamente");
            response.put("data", productoActualizado);
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Producto no encontrado para actualizar: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al actualizar producto: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al actualizar producto {}: {}", id, e.getMessage(), e);
            return buildErrorResponse("Error al actualizar producto: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            productoService.eliminar(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Producto eliminado exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Producto no encontrado para eliminar: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al eliminar producto: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al eliminar producto {}: {}", id, e.getMessage(), e);
            return buildErrorResponse("Error al eliminar producto: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.status(status).body(response);
    }
}