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
public class ProductoRequestDto {
    
    private Integer id; // Para actualización/eliminación
    
    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 30, message = "El SKU no puede exceder los 30 caracteres")
    private String sku;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede exceder los 120 caracteres")
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;
    
    @NotNull(message = "La categoría es obligatoria")
    private Integer categoriaId;
    
    @Size(max = 50, message = "El código de barras no puede exceder los 50 caracteres")
    private String codigoBarras;
    
    @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser mayor a 0")
    @Digits(integer = 16, fraction = 2, message = "El precio debe tener máximo 16 enteros y 2 decimales")
    private BigDecimal precioLista;
    
    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo;
    
    @NotNull(message = "La unidad es obligatoria")
    private Integer unidadId;
}
