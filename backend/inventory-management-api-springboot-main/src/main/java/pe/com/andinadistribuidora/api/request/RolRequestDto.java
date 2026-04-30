package pe.com.andinadistribuidora.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolRequestDto {
    
	private Integer id; //Para actualizar
	
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 30, message = "La descripción no puede exceder los 30 caracteres")
    private String descripcion;

}