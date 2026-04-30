package pe.com.andinadistribuidora.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Inventario {
    
    @EmbeddedId
    private InventarioId id;
    
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 12, fraction = 6)
    @Column(name = "cantidad", precision = 18, scale = 6, nullable = false)
    private BigDecimal cantidad;
    
    @NotNull
    @DecimalMin("0.0")
    @Digits(integer = 12, fraction = 6)
    @Column(name = "stock_min", precision = 18, scale = 6, nullable = false)
    private BigDecimal stockMin;
    
    @DecimalMin("0.0")
    @Digits(integer = 12, fraction = 6)
    @Column(name = "stock_max", precision = 18, scale = 6)
    private BigDecimal stockMax;
    
    @Column(name = "ultima_actualizacion", nullable = false)
    private LocalDateTime ultimaActualizacion;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("almacenId")
    @JoinColumn(name = "almacen_id", nullable = false)
    @JsonIgnore
    private Almacen almacen;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("productoId")
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore
    private Producto producto;
    
    // Clase interna para la clave compuesta
    @Embeddable
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    @EqualsAndHashCode
    public static class InventarioId implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        @Column(name = "almacen_id")
        private Integer almacenId;
        
        @Column(name = "producto_id")
        private Integer productoId;
    }
}