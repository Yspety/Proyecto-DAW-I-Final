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
public class CategoriaRequestDto {
	
	// El ID es opcional para crear, obligatorio para actualizar
    private Integer id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede exceder los 80 caracteres")
    private String nombre;
    
    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    private String descripcion;
    
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
}