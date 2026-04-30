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
public class AlmacenRequestDto {
    
    private Integer id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede exceder los 80 caracteres")
    private String nombre;
    
    @NotBlank(message = "El tipo es obligatorio")
    private String tipo; // String para recibir desde el formulario
    
    @Size(max = 180, message = "La dirección no puede exceder los 180 caracteres")
    private String direccion;
    
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}