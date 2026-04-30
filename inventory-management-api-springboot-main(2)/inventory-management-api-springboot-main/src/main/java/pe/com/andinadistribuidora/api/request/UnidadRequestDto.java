package pe.com.andinadistribuidora.api.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
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
public class UnidadRequestDto {
    
    private Integer id; // Para actualización/eliminación
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 10, message = "El código no puede exceder los 10 caracteres")
    private String codigo;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 40, message = "El nombre no puede exceder los 40 caracteres")
    private String nombre;
    
    @NotNull(message = "El factor base es obligatorio")
    @DecimalMin(value = "0.000001", inclusive = true, message = "El factor base debe ser mayor a 0")
    @Digits(integer = 12, fraction = 6, message = "El factor base debe tener máximo 12 enteros y 6 decimales")
    private BigDecimal factorBase;
    
    @NotNull(message = "Debe indicar si es unidad base")
    private Boolean esBase;
}