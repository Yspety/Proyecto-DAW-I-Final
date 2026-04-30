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
import pe.com.andinadistribuidora.api.request.InventarioMovimientoRequestDto;
import pe.com.andinadistribuidora.api.response.InventarioMovimientoResponseDto;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.service.InventarioMovimientoService;

@Slf4j
@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class InventarioMovimientoRestController {
    
    private final InventarioMovimientoService movimientoService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        try {
            List<InventarioMovimientoResponseDto> movimientos = movimientoService.listar();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Movimientos listados correctamente");
            response.put("data", movimientos);
            response.put("total", movimientos.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al listar movimientos: {}", e.getMessage());
            return buildErrorResponse("Error al listar movimientos: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Long id) {
        try {
            InventarioMovimientoResponseDto movimiento = movimientoService.obtener(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Movimiento encontrado");
            response.put("data", movimiento);
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Movimiento no encontrado: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al obtener movimiento {}: {}", id, e.getMessage());
            return buildErrorResponse("Error al obtener movimiento: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(
            @Validated @RequestBody InventarioMovimientoRequestDto request) {
        try {
            // Establecer fecha actual si no viene
            if (request.getFechaMovimiento() == null) {
                request.setFechaMovimiento(LocalDateTime.now());
            }
            
            // Establecer usuario por defecto si no viene
            if (request.getUsuario() == null || request.getUsuario().isEmpty()) {
                request.setUsuario("API_USER");
            }
            
            InventarioMovimientoResponseDto movimientoCreado = movimientoService.crear(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Movimiento creado exitosamente");
            response.put("data", movimientoCreado);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BusinessException e) {
            log.error("Error de negocio al crear movimiento: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            log.error("Recurso no encontrado al crear movimiento: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al crear movimiento: {}", e.getMessage(), e);
            return buildErrorResponse("Error al crear movimiento: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Long id,
            @Validated @RequestBody InventarioMovimientoRequestDto request) {
        try {
            InventarioMovimientoResponseDto movimientoActualizado = 
                movimientoService.actualizar(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Movimiento actualizado exitosamente");
            response.put("data", movimientoActualizado);
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Movimiento no encontrado para actualizar: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al actualizar movimiento: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al actualizar movimiento {}: {}", id, e.getMessage(), e);
            return buildErrorResponse("Error al actualizar movimiento: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        try {
            movimientoService.eliminar(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Movimiento eliminado exitosamente (inventario revertido)");
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Movimiento no encontrado para eliminar: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al eliminar movimiento: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al eliminar movimiento {}: {}", id, e.getMessage(), e);
            return buildErrorResponse("Error al eliminar movimiento: " + e.getMessage(), 
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