package pe.com.andinadistribuidora.entity;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusu")
    private Integer id;
    
    @NotBlank @Size(max = 50)
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;
    
    @NotBlank @Size(max = 50)
    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;
    
    @NotBlank @Size(max = 100)
    @Column(name = "usuario", length = 100, nullable = false, unique = true)
    private String usuario;
    
    @NotBlank @Size(max = 255)
    @Column(name = "password", length = 255, nullable = false)
    private String password;
    
    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idrol", nullable = false,
        foreignKey = @ForeignKey(name = "FK_RolUsuario"))
    @JsonIgnore
    private Rol rol;
}