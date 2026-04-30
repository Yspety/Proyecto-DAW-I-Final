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
public class UnidadResponseDto {
    
    private Integer id;
    private String codigo;
    private String nombre;
    private BigDecimal factorBase;
    private Boolean esBase;
    private LocalDateTime ultimaActualizacion;
}