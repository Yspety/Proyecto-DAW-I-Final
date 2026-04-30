package pe.com.andinadistribuidora.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "producto")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
    private Integer id;
    
    @NotBlank
    @Size(max = 30)
    @Column(name = "sku", length = 30, nullable = false, unique = true)
    private String sku;
    
    @NotBlank
    @Size(max = 120)
    @Column(name = "nombre", length = 120, nullable = false)
    private String nombre;
    
    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    
    @Column(name = "categoria_id", insertable = false, updatable = false)
    private Integer categoriaId;
    
    @Size(max = 50)
    @Column(name = "codigo_barras", length = 50)
    private String codigoBarras;
    
    @DecimalMin("0.01")
    @Digits(integer = 16, fraction = 2)
    @Column(name = "precio_lista", precision = 18, scale = 2)
    private BigDecimal precioLista;
    
    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;
    
    @Column(name = "ultima_actualizacion", nullable = false)
    private LocalDateTime ultimaActualizacion;
    
    @Column(name = "unidad_id", insertable = false, updatable = false)
    private Integer unidadId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_producto_categoria"))
    @JsonIgnore
    private Categoria categoria;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidad_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_producto_unidad"))
    @JsonIgnore
    private Unidad unidad;
}