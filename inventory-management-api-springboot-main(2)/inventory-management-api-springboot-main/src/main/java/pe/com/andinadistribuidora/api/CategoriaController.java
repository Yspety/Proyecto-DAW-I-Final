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
import pe.com.andinadistribuidora.api.request.CategoriaRequestDto;
import pe.com.andinadistribuidora.service.CategoriaService;

@Slf4j
@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lstCategorias", categoriaService.listar());
        model.addAttribute("categoriaRequestDto", new CategoriaRequestDto());
        return "listCategoria";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute CategoriaRequestDto categoriaRequestDto,
                         RedirectAttributes redirectAttributes) {
        try {
            categoriaService.crear(categoriaRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría creada exitosamente");
        } catch (Exception e) {
            log.error("Error al crear categoría: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            model.addAttribute("categoriaRequestDto", categoriaService.obtener(id));
            model.addAttribute("lstCategorias", categoriaService.listar());
            model.addAttribute("editando", true);
        } catch (Exception e) {
            log.error("Error al obtener categoría {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Categoría no encontrada");
            return "redirect:/categorias";
        }
        return "crudcategorias";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute CategoriaRequestDto categoriaRequestDto,
                           RedirectAttributes redirectAttributes) {
        try {
            if (categoriaRequestDto.getId() == null) {
                throw new IllegalArgumentException("El ID de la categoría es obligatorio");
            }
            categoriaService.actualizar(categoriaRequestDto.getId(), categoriaRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría actualizada exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar categoría: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                          RedirectAttributes redirectAttributes) {
        try {
            categoriaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Categoría eliminada exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar categoría {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/categorias";
    }
}
