package pe.com.andinadistribuidora.api.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioRequestDto {
    
    // Para actualización/eliminación (clave compuesta)
    private Integer almacenId;
    private Integer productoId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.0", message = "La cantidad no puede ser negativa")
    @Digits(integer = 12, fraction = 6)
    private BigDecimal cantidad;
    
    @NotNull(message = "El stock mínimo es obligatorio")
    @DecimalMin(value = "0.0", message = "El stock mínimo no puede ser negativo")
    @Digits(integer = 12, fraction = 6)
    private BigDecimal stockMin;
    
    @DecimalMin(value = "0.0", message = "El stock máximo no puede ser negativo")
    @Digits(integer = 12, fraction = 6)
    private BigDecimal stockMax;
}