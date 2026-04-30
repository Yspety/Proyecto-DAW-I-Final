package pe.com.andinadistribuidora.api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.response.RolResponseDto;
import pe.com.andinadistribuidora.service.RolService;

@Slf4j
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolRestController {

    private final RolService rolService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        try {
            List<RolResponseDto> roles = rolService.listar();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Roles listados correctamente");
            response.put("data", roles);
            response.put("total", roles.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al listar roles: {}", e.getMessage());

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al listar roles: " + e.getMessage());
            error.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
