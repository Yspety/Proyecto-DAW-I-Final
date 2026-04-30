package pe.com.andinadistribuidora.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import pe.com.andinadistribuidora.entity.Usuario;

/**
 *
 *
 * Objeto de valor inmutable: extrae todos los campos del Usuario dentro
 * del constructor (que corre con sesión JPA abierta) para evitar
 * LazyInitializationException cuando Spring Security accede a los datos
 *
 *
 *
 *
 *
 * fuera de la transacción.
 */
public class UsuarioPrincipal implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final boolean activo;
    private final String nombre;
    private final String apellido;
    private final String rolDescripcion;

    public UsuarioPrincipal(Usuario usuario) {
        this.id             = usuario.getId();
        this.username       = usuario.getUsuario();
        this.password       = usuario.getPassword();
        this.activo         = usuario.getActivo();
        this.nombre         = usuario.getNombre();
        this.apellido       = usuario.getApellido();
        this.rolDescripcion = usuario.getRol().getDescripcion(); // dentro de la transacción → seguro
    }

    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rolDescripcion));
    }

    @Override public boolean isEnabled()                { return activo; }
    @Override public boolean isAccountNonExpired()      { return true; }
    @Override public boolean isAccountNonLocked()       { return true; }
    @Override public boolean isCredentialsNonExpired()  { return true; }

    public String  getNombreCompleto() { return nombre + " " + apellido; }
    public String  getRolDescripcion() { return rolDescripcion; }
    public Integer getUsuarioId()      { return id; }
}
