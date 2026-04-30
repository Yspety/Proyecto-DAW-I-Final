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
import pe.com.andinadistribuidora.api.request.InventarioRequestDto;
import pe.com.andinadistribuidora.api.response.InventarioResponseDto;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.service.InventarioService;

@Slf4j
@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioRestController {

    private final InventarioService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        try {
            List<InventarioResponseDto> inventario = service.listar();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Inventario listado correctamente");
            response.put("data", inventario);
            response.put("total", inventario.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al listar inventario: {}", e.getMessage());
            return buildErrorResponse("Error al listar inventario: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{almacenId}/{productoId}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Integer almacenId,
                                                       @PathVariable Integer productoId) {
        try {
            log.info("GET /api/inventario/{}/{}", almacenId, productoId);
            InventarioResponseDto inventario = service.obtener(almacenId, productoId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Inventario encontrado");
            response.put("data", inventario);

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Inventario no encontrado: almacen={}, producto={}", almacenId, productoId);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al obtener inventario {}/{}: {}", almacenId, productoId, e.getMessage());
            return buildErrorResponse("Error al obtener inventario: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody InventarioRequestDto request) {
        try {
            log.info("POST /api/inventario almacen={}, producto={}", request.getAlmacenId(), request.getProductoId());
            InventarioResponseDto saved = service.crear(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Inventario creado exitosamente");
            response.put("data", saved);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BusinessException e) {
            log.error("Error de negocio al crear inventario: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            log.error("Recurso no encontrado al crear inventario: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al crear inventario: {}", e.getMessage(), e);
            return buildErrorResponse("Error al crear inventario: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{almacenId}/{productoId}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Integer almacenId,
                                                          @PathVariable Integer productoId,
                                                          @Valid @RequestBody InventarioRequestDto request) {
        try {
            log.info("PUT /api/inventario/{}/{}", almacenId, productoId);
            InventarioResponseDto updated = service.actualizar(almacenId, productoId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Inventario actualizado exitosamente");
            response.put("data", updated);

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Inventario no encontrado para actualizar: {}/{}", almacenId, productoId);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al actualizar inventario: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al actualizar inventario {}/{}: {}", almacenId, productoId, e.getMessage(), e);
            return buildErrorResponse("Error al actualizar inventario: " + e.getMessage(),
                                     HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{almacenId}/{productoId}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer almacenId,
                                                        @PathVariable Integer productoId) {
        try {
            log.info("DELETE /api/inventario/{}/{}", almacenId, productoId);
            service.eliminar(almacenId, productoId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Inventario eliminado exitosamente");

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Inventario no encontrado para eliminar: {}/{}", almacenId, productoId);
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al eliminar inventario: {}", e.getMessage());
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al eliminar inventario {}/{}: {}", almacenId, productoId, e.getMessage(), e);
            return buildErrorResponse("Error al eliminar inventario: " + e.getMessage(),
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
