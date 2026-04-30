package pe.com.andinadistribuidora.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponseDto {
    
    private Integer almacenId;
    private String almacenNombre;
    
    private Integer productoId;
    private String productoNombre;
    private String productoSku;
    
    private BigDecimal cantidad;
    private BigDecimal stockMin;
    private BigDecimal stockMax;
    
    private LocalDateTime ultimaActualizacion;
    
    // Campos útiles para alertas
    private Boolean bajStock; // true si cantidad < stockMin
    private Boolean stockCritico; // true si cantidad <= stockMin * 0.5
}
