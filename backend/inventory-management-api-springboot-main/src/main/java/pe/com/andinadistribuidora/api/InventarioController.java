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
import pe.com.andinadistribuidora.api.request.InventarioRequestDto;
import pe.com.andinadistribuidora.service.AlmacenService;
import pe.com.andinadistribuidora.service.InventarioService;
import pe.com.andinadistribuidora.service.ProductoService;

@Slf4j
@Controller
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;
    private final AlmacenService almacenService;
    private final ProductoService productoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lstInventario", inventarioService.listar());
        model.addAttribute("lstAlmacenes", almacenService.listar());
        model.addAttribute("lstProductos", productoService.listar());
        model.addAttribute("lstStockBajo", inventarioService.listarStockBajo());
        model.addAttribute("lstStockCritico", inventarioService.listarStockCritico());
        model.addAttribute("inventarioRequestDto", new InventarioRequestDto());
        return "listInventario.html";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute InventarioRequestDto inventarioRequestDto,
                         RedirectAttributes redirectAttributes) {
        try {
            inventarioService.crear(inventarioRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario creado exitosamente");
        } catch (Exception e) {
            log.error("Error al crear inventario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventario";
    }

    @GetMapping("/editar/{almacenId}/{productoId}")
    public String editar(@PathVariable Integer almacenId,
                        @PathVariable Integer productoId,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            model.addAttribute("inventarioRequestDto", inventarioService.obtener(almacenId, productoId));
            model.addAttribute("lstInventario", inventarioService.listar());
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("editando", true);
        } catch (Exception e) {
            log.error("Error al obtener inventario almacen={}, producto={}: {}", almacenId, productoId, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Inventario no encontrado");
            return "redirect:/inventario";
        }
        return "listInventario";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute InventarioRequestDto inventarioRequestDto,
                           RedirectAttributes redirectAttributes) {
        try {
            if (inventarioRequestDto.getAlmacenId() == null || inventarioRequestDto.getProductoId() == null) {
                throw new IllegalArgumentException("El almacén y producto son obligatorios");
            }
            inventarioService.actualizar(
                inventarioRequestDto.getAlmacenId(),
                inventarioRequestDto.getProductoId(),
                inventarioRequestDto
            );
            redirectAttributes.addFlashAttribute("mensaje", "Inventario actualizado exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar inventario: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventario";
    }

    @GetMapping("/eliminar/{almacenId}/{productoId}")
    public String eliminar(@PathVariable Integer almacenId,
                          @PathVariable Integer productoId,
                          RedirectAttributes redirectAttributes) {
        try {
            inventarioService.eliminar(almacenId, productoId);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario eliminado exitosamente");
        } catch (Exception e) {
            log.error("Error al eliminar inventario almacen={}, producto={}: {}", almacenId, productoId, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventario";
    }

    @GetMapping("/almacen/{almacenId}")
    public String listarPorAlmacen(@PathVariable Integer almacenId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("lstInventario", inventarioService.listarPorAlmacen(almacenId));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("almacenSeleccionado", almacenId);
            model.addAttribute("inventarioRequestDto", new InventarioRequestDto());
            return "listInventario";
        } catch (Exception e) {
            log.error("Error al listar inventario por almacén {}: {}", almacenId, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/inventario";
        }
    }

    @GetMapping("/producto/{productoId}")
    public String listarPorProducto(@PathVariable Integer productoId,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("lstInventario", inventarioService.listarPorProducto(productoId));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("productoSeleccionado", productoId);
            model.addAttribute("inventarioRequestDto", new InventarioRequestDto());
            return "listInventario";
        } catch (Exception e) {
            log.error("Error al listar inventario por producto {}: {}", productoId, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/inventario";
        }
    }

    @GetMapping("/stock-bajo")
    public String stockBajo(Model model) {
        model.addAttribute("lstInventario", inventarioService.listarStockBajo());
        model.addAttribute("lstAlmacenes", almacenService.listar());
        model.addAttribute("lstProductos", productoService.listar());
        model.addAttribute("filtroActivo", "STOCK_BAJO");
        model.addAttribute("inventarioRequestDto", new InventarioRequestDto());
        return "listInventario";
    }

    @GetMapping("/stock-critico")
    public String stockCritico(Model model) {
        model.addAttribute("lstInventario", inventarioService.listarStockCritico());
        model.addAttribute("lstAlmacenes", almacenService.listar());
        model.addAttribute("lstProductos", productoService.listar());
        model.addAttribute("filtroActivo", "STOCK_CRITICO");
        model.addAttribute("inventarioRequestDto", new InventarioRequestDto());
        return "listInventario";
    }
}
