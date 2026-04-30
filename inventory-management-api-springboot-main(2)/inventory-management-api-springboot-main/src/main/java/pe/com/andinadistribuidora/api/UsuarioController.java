package pe.com.andinadistribuidora.api;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.UsuarioRequestDto;
import pe.com.andinadistribuidora.api.response.UsuarioResponseDto;
import pe.com.andinadistribuidora.config.UsuarioPrincipal;
import pe.com.andinadistribuidora.service.RolService;
import pe.com.andinadistribuidora.service.UsuarioService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    // ========================================
    // AUTENTICACIÓN — manejada por Spring Security
    // ========================================

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        if (authentication != null) {
            UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
            model.addAttribute("usuario", usuarioService.obtener(principal.getUsuarioId()));
        }
        return "home";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }

    // ========================================
    // CRUD DE USUARIOS (Solo ADMIN — protegido por SecurityConfig)
    // ========================================

    @GetMapping("/usuarios")
    public String listar(Model model) {
        model.addAttribute("lstUsuarios", usuarioService.listar());
        model.addAttribute("lstRoles", rolService.listar());
        model.addAttribute("usuarioRequestDto", new UsuarioRequestDto());
        return "listUsuario";
    }

    @PostMapping("/usuarios/guardar")
    public String guardar(@ModelAttribute UsuarioRequestDto usuarioRequestDto,
                          RedirectAttributes redirectAttributes) {
        try {
            usuarioService.crear(usuarioRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
        } catch (Exception e) {
            log.error("Error al crear usuario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editar(@PathVariable Integer id,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        try {
            UsuarioResponseDto usuario = usuarioService.obtener(id);
            model.addAttribute("usuarioRequestDto", usuario);
            model.addAttribute("lstUsuarios", usuarioService.listar());
            model.addAttribute("lstRoles", rolService.listar());
            model.addAttribute("editando", true);
        } catch (Exception e) {
            log.error("Error al obtener usuario {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            return "redirect:/usuarios";
        }
        return "crudusuarios";
    }

    @PostMapping("/usuarios/actualizar")
    public String actualizar(@ModelAttribute UsuarioRequestDto usuarioRequestDto,
                             @RequestParam Integer id,
                             RedirectAttributes redirectAttributes) {
        try {
            usuarioService.actualizar(id, usuarioRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar usuario {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        if (principal.getUsuarioId().equals(id)) {
            redirectAttributes.addFlashAttribute("error", "No puedes eliminar tu propia cuenta");
            return "redirect:/usuarios";
        }

        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar usuario {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
