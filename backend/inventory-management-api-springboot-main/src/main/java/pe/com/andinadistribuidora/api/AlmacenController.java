package pe.com.andinadistribuidora.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.AlmacenRequestDto;
import pe.com.andinadistribuidora.service.AlmacenService;

@Slf4j
@Controller
@RequestMapping("/almacenes")
@RequiredArgsConstructor
public class AlmacenController {

    private final AlmacenService almacenService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lstAlmacenes", almacenService.listar());
        model.addAttribute("almacenRequestDto", new AlmacenRequestDto());
        return "listAlmacen";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute AlmacenRequestDto almacenRequestDto,
                         RedirectAttributes redirectAttributes) {
        try {
            almacenService.crear(almacenRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Almacén creado exitosamente");
        } catch (Exception e) {
            log.error("Error al crear almacén: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/almacenes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            model.addAttribute("almacenRequestDto", almacenService.obtener(id));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("editando", true);
        } catch (Exception e) {
            log.error("Error al obtener almacén {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Almacén no encontrado");
            return "redirect:/almacenes";
        }
        return "crudalmacenes";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute AlmacenRequestDto almacenRequestDto,
                           RedirectAttributes redirectAttributes) {
        try {
            if (almacenRequestDto.getId() == null) {
                throw new IllegalArgumentException("El ID del almacén es obligatorio");
            }
            almacenService.actualizar(almacenRequestDto.getId(), almacenRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Almacén actualizado exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar almacén: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/almacenes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                          RedirectAttributes redirectAttributes) {
        try {
            almacenService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Almacén eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar almacén {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/almacenes";
    }
}
