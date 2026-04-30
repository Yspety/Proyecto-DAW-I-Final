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
public class InventarioMovimientoResponseDto {
    
    private Long id;
    private LocalDateTime fechaMovimiento;
    
    private Integer almacenId;
    private String almacenNombre;
    
    private Integer productoId;
    private String productoNombre;
    private String productoSku;
    
    private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE
    private BigDecimal cantidad;
    private BigDecimal costoUnitario;
    private BigDecimal costoTotal; // cantidad * costoUnitario
    
    private String referencia;
    private String usuario;
    
    private LocalDateTime ultimaActualizacion;
}