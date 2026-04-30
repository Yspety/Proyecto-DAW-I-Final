package pe.com.andinadistribuidora.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import pe.com.andinadistribuidora.api.request.RolRequestDto;
import pe.com.andinadistribuidora.service.RolService;

@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolController {
    
    private final RolService rolService;
    
    // --- 1. LISTAR ROLES ---
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lstRoles", rolService.listar());
        model.addAttribute("rolRequestDto", new RolRequestDto());
        return "crudroles";
    }
    
    // --- 2. CREAR ROL ---
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute RolRequestDto rolRequestDto, Model model) {
        rolService.crear(rolRequestDto); // Validación y persistencia en el service
        model.addAttribute("lstRoles", rolService.listar());
        model.addAttribute("rolRequestDto", new RolRequestDto());
        return "crudroles";
    }
    
    // --- 3. EDITAR ROL (mostrar datos en formulario) ---
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("rolRequestDto", rolService.obtener(id));
        model.addAttribute("lstRoles", rolService.listar());
        return "crudroles";
    }
    
    // --- 4. ACTUALIZAR ROL ---
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute RolRequestDto rolRequestDto, Model model) {
        // Nota: Necesitarás pasar el ID. Puedes agregar un campo oculto en el form o modificar el DTO
        // Por ahora, asumo que agregarás un campo 'id' en RolRequestDto para la actualización
        rolService.actualizar(rolRequestDto.getId(), rolRequestDto);
        model.addAttribute("lstRoles", rolService.listar());
        model.addAttribute("rolRequestDto", new RolRequestDto());
        return "crudroles";
    }
    
    // --- 5. ELIMINAR ROL ---
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id, Model model) {
        rolService.eliminar(id);
        model.addAttribute("lstRoles", rolService.listar());
        model.addAttribute("rolRequestDto", new RolRequestDto());
        return "crudroles";
    }
}