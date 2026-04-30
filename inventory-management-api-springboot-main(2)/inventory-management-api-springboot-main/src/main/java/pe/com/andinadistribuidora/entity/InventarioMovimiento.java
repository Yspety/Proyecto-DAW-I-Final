package pe.com.andinadistribuidora.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventario_movimiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InventarioMovimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventario_movimiento_id")
    private Long id;
    
    @NotNull
    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;
    
    @Column(name = "almacen_id", insertable = false, updatable = false)
    private Integer almacenId;
    
    @Column(name = "producto_id", insertable = false, updatable = false)
    private Integer productoId;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private TipoMovimiento tipoMovimiento;
    
    @NotNull
    @DecimalMin("0.000001")
    @Digits(integer = 12, fraction = 6)
    @Column(name = "cantidad", precision = 18, scale = 6, nullable = false)
    private BigDecimal cantidad;
    
    @DecimalMin("0.0")
    @Digits(integer = 12, fraction = 6)
    @Column(name = "costo_unitario", precision = 18, scale = 6)
    private BigDecimal costoUnitario;
    
    @Size(max = 100)
    @Column(name = "referencia", length = 100)
    private String referencia;
    
    @Size(max = 60)
    @Column(name = "usuario", length = 60)
    private String usuario;
    
    @Column(name = "ultima_actualizacion", nullable = false)
    private LocalDateTime ultimaActualizacion;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "almacen_id", nullable = false,
        foreignKey = @ForeignKey(name = "inventario_movimiento_ibfk_1"))
    @JsonIgnore
    private Almacen almacen;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false,
        foreignKey = @ForeignKey(name = "inventario_movimiento_ibfk_2"))
    @JsonIgnore
    private Producto producto;
    
    // Enum para el tipo de movimiento
    public enum TipoMovimiento {
        ENTRADA,
        SALIDA,
        AJUSTE
    }
}