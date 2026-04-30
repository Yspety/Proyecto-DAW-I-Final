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
public class ProductoResponseDto {
    
    private Integer id;
    private String sku;
    private String nombre;
    private String descripcion;
    private Integer categoriaId;
    private String categoriaNombre;
    private String codigoBarras;
    private BigDecimal precioLista;
    private Boolean activo;
    private LocalDateTime creadoEn;
    private LocalDateTime ultimaActualizacion;
    private Integer unidadId;
    private String unidadNombre;
}