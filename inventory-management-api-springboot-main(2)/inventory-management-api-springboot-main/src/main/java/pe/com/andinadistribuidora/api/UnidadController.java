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
import pe.com.andinadistribuidora.api.request.UnidadRequestDto;
import pe.com.andinadistribuidora.service.UnidadService;

@Slf4j
@Controller
@RequestMapping("/unidades")
@RequiredArgsConstructor
public class UnidadController {

    private final UnidadService unidadService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lstUnidades", unidadService.listar());
        model.addAttribute("unidadRequestDto", new UnidadRequestDto());
        return "listUnidad";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute UnidadRequestDto unidadRequestDto,
                         RedirectAttributes redirectAttributes) {
        try {
            unidadService.crear(unidadRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Unidad creada exitosamente");
        } catch (Exception e) {
            log.error("Error al crear unidad: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/unidades";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            model.addAttribute("unidadRequestDto", unidadService.obtener(id));
            model.addAttribute("lstUnidades", unidadService.listar());
            model.addAttribute("editando", true);
        } catch (Exception e) {
            log.error("Error al obtener unidad {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Unidad no encontrada");
            return "redirect:/unidades";
        }
        return "crudunidades";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute UnidadRequestDto unidadRequestDto,
                           RedirectAttributes redirectAttributes) {
        try {
            if (unidadRequestDto.getId() == null) {
                throw new IllegalArgumentException("El ID de la unidad es obligatorio");
            }
            unidadService.actualizar(unidadRequestDto.getId(), unidadRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Unidad actualizada exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar unidad: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/unidades";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                          RedirectAttributes redirectAttributes) {
        try {
            unidadService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Unidad eliminada exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar unidad {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/unidades";
    }
}
