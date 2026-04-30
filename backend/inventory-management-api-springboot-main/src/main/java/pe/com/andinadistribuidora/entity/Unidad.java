package pe.com.andinadistribuidora.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "unidad")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Unidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unidad_id")
    private Integer id;
    
    @NotBlank
    @Size(max = 10)
    @Column(name = "codigo", length = 10, nullable = false, unique = true)
    private String codigo;
    
    @NotBlank
    @Size(max = 40)
    @Column(name = "nombre", length = 40, nullable = false)
    private String nombre;
    
    @NotNull
    @DecimalMin("0.000001")
    @Digits(integer = 12, fraction = 6)
    @Column(name = "factor_base", precision = 18, scale = 6, nullable = false)
    private BigDecimal factorBase;
    
    @NotNull
    @Column(name = "es_base", nullable = false)
    private Boolean esBase;
    
    @Column(name = "ultima_actualizacion", nullable = false)
    private LocalDateTime ultimaActualizacion;
}