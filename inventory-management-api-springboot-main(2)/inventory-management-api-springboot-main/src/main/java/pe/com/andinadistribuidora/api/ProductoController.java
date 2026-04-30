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
import pe.com.andinadistribuidora.api.request.ProductoRequestDto;
import pe.com.andinadistribuidora.service.CategoriaService;
import pe.com.andinadistribuidora.service.ProductoService;
import pe.com.andinadistribuidora.service.UnidadService;

@Slf4j
@Controller
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final UnidadService unidadService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lstProductos", productoService.listar());
        model.addAttribute("lstCategorias", categoriaService.listar());
        model.addAttribute("lstUnidades", unidadService.listar());
        model.addAttribute("productoRequestDto", new ProductoRequestDto());
        return "listProducto";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ProductoRequestDto productoRequestDto,
                         RedirectAttributes redirectAttributes) {
        try {
            productoService.crear(productoRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto creado exitosamente");
        } catch (Exception e) {
            log.error("Error al crear producto: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            model.addAttribute("productoRequestDto", productoService.obtener(id));
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("lstCategorias", categoriaService.listar());
            model.addAttribute("lstUnidades", unidadService.listar());
            model.addAttribute("editando", true);
        } catch (Exception e) {
            log.error("Error al obtener producto {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/productos";
        }
        return "crudproductos";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute ProductoRequestDto productoRequestDto,
                           RedirectAttributes redirectAttributes) {
        try {
            if (productoRequestDto.getId() == null) {
                throw new IllegalArgumentException("El ID del producto es obligatorio");
            }
            productoService.actualizar(productoRequestDto.getId(), productoRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar producto: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id,
                          RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar producto {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/productos";
    }
}
