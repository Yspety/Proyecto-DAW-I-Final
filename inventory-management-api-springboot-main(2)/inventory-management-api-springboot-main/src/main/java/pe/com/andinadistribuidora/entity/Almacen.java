package pe.com.andinadistribuidora.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "almacen")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Almacen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "almacen_id")
    private Integer id;
    
    @NotBlank
    @Size(max = 80)
    @Column(name = "nombre", length = 80, nullable = false)
    private String nombre;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoAlmacen tipo;
    
    @Size(max = 180)
    @Column(name = "direccion", length = 180)
    private String direccion;
    
    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @Column(name = "ultima_actualizacion", nullable = false)
    private LocalDateTime ultimaActualizacion;
    
    // Enum para el tipo de almacén
    public enum TipoAlmacen {
        PRINCIPAL,
        SECUNDARIO,
        TEMPORAL
    }
}