package pe.com.andinadistribuidora.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.AlmacenRequestDto;
import pe.com.andinadistribuidora.api.response.AlmacenResponseDto;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.service.AlmacenService;

@Slf4j
@RestController
@RequestMapping("/api/almacenes")
@RequiredArgsConstructor
public class AlmacenRestController {

    private final AlmacenService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        try {
            List<AlmacenResponseDto> almacenes = service.listar();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Almacenes listados correctamente");
            response.put("data", almacenes);
            response.put("total", almacenes.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al listar almacenes: {}", e.getMessage());
            return buildErrorResponse("Error al listar almacenes: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<Map<String, Object>> listarActivos() {
        try {
            List<AlmacenResponseDto> almacenes = service.listarActivos();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Almacenes activos listados correctamente");
            response.put("data", almacenes);
            response.put("total", almacenes.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al listar almacenes activos: {}", e.getMessage());
            return buildErrorResponse("Error al listar almacenes activos: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Integer id) {
        try {
            AlmacenResponseDto almacen = service.obtener(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Almacén encontrado");
            response.put("data", almacen);

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Almacén no encontrado: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al obtener almacén {}: {}", id, e.getMessage());
            return buildErrorResponse("Error al obtener almacén: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody AlmacenRequestDto request) {
        try {
            log.info("POST /api/almacenes nombre='{}'", request.getNombre());
            AlmacenResponseDto saved = service.crear(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Almacén creado exitosamente");
            response.put("data", saved);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BusinessException e) {
            log.error("Error de negocio al crear almacén: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al crear almacén: {}", e.getMessage(), e);
            return buildErrorResponse("Error al crear almacén: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Integer id,
                                                          @Valid @RequestBody AlmacenRequestDto request) {
        try {
            log.info("PUT /api/almacenes/{}", id);
            AlmacenResponseDto updated = service.actualizar(id, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Almacén actualizado exitosamente");
            response.put("data", updated);

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Almacén no encontrado para actualizar: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al actualizar almacén: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al actualizar almacén {}: {}", id, e.getMessage(), e);
            return buildErrorResponse("Error al actualizar almacén: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            log.info("DELETE /api/almacenes/{}", id);
            service.eliminar(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Almacén eliminado exitosamente");

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Almacén no encontrado para eliminar: {}", id);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al eliminar almacén: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al eliminar almacén {}: {}", id, e.getMessage(), e);
            return buildErrorResponse("Error al eliminar almacén: " + e.getMessage(),
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
