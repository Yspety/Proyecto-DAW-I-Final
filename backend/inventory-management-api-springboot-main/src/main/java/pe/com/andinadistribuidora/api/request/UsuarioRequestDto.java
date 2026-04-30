package pe.com.andinadistribuidora.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDto {
    
    // El ID no se incluye en el Request porque es autogenerado
    // Si necesitas un DTO para actualización, podrías crear otro DTO separado
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    private String apellido;
    
    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 100, message = "El usuario no puede exceder los 100 caracteres")
    private String usuario;
    
    @Size(min = 4, max = 255, message = "La contraseña debe tener entre 4 y 255 caracteres")
    private String password;
    
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
    
    @NotNull(message = "El rolId es obligatorio")
    private Integer rolId;
    
    // El campo creadoEn no se incluye porque se genera automáticamente en la BD
}