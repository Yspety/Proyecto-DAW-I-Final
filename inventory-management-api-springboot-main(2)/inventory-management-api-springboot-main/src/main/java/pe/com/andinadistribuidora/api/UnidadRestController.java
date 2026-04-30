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
import pe.com.andinadistribuidora.api.request.UnidadRequestDto;
import pe.com.andinadistribuidora.api.response.UnidadResponseDto;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.service.UnidadService;

@Slf4j
@RestController
@RequestMapping("/api/unidades")
@RequiredArgsConstructor
public class UnidadRestController {
    
    private final UnidadService unidadService;
    
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        try {
            List<UnidadResponseDto> unidades = unidadService.listar();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Unidades listadas correctamente");
            response.put("data", unidades);
            response.put("total", unidades.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al listar unidades: {}", e.getMessage());
            return buildErrorResponse("Error al listar unidades: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Integer id) {
        try {
            UnidadResponseDto unidad = unidadService.obtener(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Unidad encontrada");
            response.put("data", unidad);
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Unidad no encontrada: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al obtener unidad {}: {}", id, e.getMessage());
            return buildErrorResponse("Error al obtener unidad: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(
            @Validated @RequestBody UnidadRequestDto request) {
        try {
            UnidadResponseDto unidadCreada = unidadService.crear(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Unidad creada exitosamente");
            response.put("data", unidadCreada);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BusinessException e) {
            log.error("Error de negocio al crear unidad: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al crear unidad: {}", e.getMessage(), e);
            return buildErrorResponse("Error al crear unidad: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Integer id,
            @Validated @RequestBody UnidadRequestDto request) {
        try {
            UnidadResponseDto unidadActualizada = unidadService.actualizar(id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Unidad actualizada exitosamente");
            response.put("data", unidadActualizada);
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Unidad no encontrada para actualizar: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al actualizar unidad: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al actualizar unidad {}: {}", id, e.getMessage(), e);
            return buildErrorResponse("Error al actualizar unidad: " + e.getMessage(), 
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            unidadService.eliminar(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Unidad eliminada exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Unidad no encontrada para eliminar: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al eliminar unidad: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al eliminar unidad {}: {}", id, e.getMessage(), e);
            return buildErrorResponse("Error al eliminar unidad: " + e.getMessage(), 
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