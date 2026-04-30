package pe.com.andinadistribuidora.api.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDto {
    
    private Integer id;
    private String nombre;
    private String apellido;
    private String usuario;
    // NO incluimos el password por seguridad
    private Boolean activo;
    private LocalDateTime creadoEn;
    private Integer rolId;
    private String rolDescripcion; // descripcion del rol (conveniente para la UI)
}