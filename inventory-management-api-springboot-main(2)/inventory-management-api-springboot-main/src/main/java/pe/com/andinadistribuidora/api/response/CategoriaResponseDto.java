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
public class CategoriaResponseDto {
    
    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private LocalDateTime ultimaActualizacion;
}