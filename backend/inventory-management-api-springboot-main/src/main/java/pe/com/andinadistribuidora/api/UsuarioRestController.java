package pe.com.andinadistribuidora.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import pe.com.andinadistribuidora.api.request.UsuarioRequestDto;
import pe.com.andinadistribuidora.api.response.UsuarioResponseDto;
import pe.com.andinadistribuidora.config.UsuarioPrincipal;
import pe.com.andinadistribuidora.exception.BusinessException;
import pe.com.andinadistribuidora.exception.NotFoundException;
import pe.com.andinadistribuidora.service.UsuarioService;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        try {
            UsuarioResponseDto usuario = usuarioService.obtener(principal.getUsuarioId());
            return buildOk("Usuario autenticado", usuario);
        } catch (Exception e) {
            log.error("Error al obtener usuario autenticado: {}", e.getMessage());
            return buildError("Error al obtener usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> listar() {
        try {
            List<UsuarioResponseDto> usuarios = usuarioService.listar();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuarios listados correctamente");
            response.put("data", usuarios);
            response.put("total", usuarios.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al listar usuarios: {}", e.getMessage());
            return buildError("Error al listar usuarios: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Integer id) {
        try {
            return buildOk("Usuario encontrado", usuarioService.obtener(id));
        } catch (NotFoundException e) {
            log.error("Usuario no encontrado: {}", id);
            return buildError(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al obtener usuario {}: {}", id, e.getMessage());
            return buildError("Error al obtener usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> crear(
            @Validated @RequestBody UsuarioRequestDto request) {
        try {
            UsuarioResponseDto creado = usuarioService.crear(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario creado exitosamente");
            response.put("data", creado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BusinessException e) {
            log.error("Error de negocio al crear usuario: {}", e.getMessage());
            return buildError(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            log.error("Recurso no encontrado al crear usuario: {}", e.getMessage());
            return buildError(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error al crear usuario: {}", e.getMessage(), e);
            return buildError("Error al crear usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> actualizar(
            @PathVariable Integer id,
            @Validated @RequestBody UsuarioRequestDto request) {
        try {
            return buildOk("Usuario actualizado exitosamente", usuarioService.actualizar(id, request));
        } catch (NotFoundException e) {
            log.error("Usuario no encontrado para actualizar: {}", id);
            return buildError(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al actualizar usuario: {}", e.getMessage());
            return buildError(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al actualizar usuario {}: {}", id, e.getMessage(), e);
            return buildError("Error al actualizar usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            usuarioService.eliminar(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");

            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            log.error("Usuario no encontrado para eliminar: {}", id);
            return buildError(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            log.error("Error de negocio al eliminar usuario: {}", e.getMessage());
            return buildError(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error al eliminar usuario {}: {}", id, e.getMessage(), e);
            return buildError("Error al eliminar usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Map<String, Object>> buildOk(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, Object>> buildError(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }
}
