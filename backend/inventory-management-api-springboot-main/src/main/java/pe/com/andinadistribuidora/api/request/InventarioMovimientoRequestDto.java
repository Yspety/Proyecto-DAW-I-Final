package pe.com.andinadistribuidora.api.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class InventarioMovimientoRequestDto {
    
    // Para actualización/eliminación
    private Long id;
    
    //@NotNull(message = "La fecha de movimiento es obligatoria")
    private LocalDateTime fechaMovimiento;
    
    @NotNull(message = "El almacén es obligatorio")
    private Integer almacenId;
    
    @NotNull(message = "El producto es obligatorio")
    private Integer productoId;
    
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE
    
    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.000001", message = "La cantidad debe ser mayor a cero")
    @Digits(integer = 12, fraction = 6)
    private BigDecimal cantidad;
    
    @DecimalMin(value = "0.0", message = "El costo unitario no puede ser negativo")
    @Digits(integer = 12, fraction = 6)
    private BigDecimal costoUnitario;
    
    @Size(max = 100, message = "La referencia no puede exceder 100 caracteres")
    private String referencia;
    
    @Size(max = 60, message = "El usuario no puede exceder 60 caracteres")
    private String usuario;
}