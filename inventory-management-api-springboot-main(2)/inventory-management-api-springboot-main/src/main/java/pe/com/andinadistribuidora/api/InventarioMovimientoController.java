package pe.com.andinadistribuidora.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pe.com.andinadistribuidora.api.request.InventarioMovimientoRequestDto;
import pe.com.andinadistribuidora.config.UsuarioPrincipal;
import pe.com.andinadistribuidora.service.AlmacenService;
import pe.com.andinadistribuidora.service.InventarioMovimientoService;
import pe.com.andinadistribuidora.service.ProductoService;

@Slf4j
@Controller
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class InventarioMovimientoController {

    private final InventarioMovimientoService movimientoService;
    private final AlmacenService almacenService;
    private final ProductoService productoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lstMovimientos", movimientoService.listarUltimosMovimientos());
        model.addAttribute("lstAlmacenes", almacenService.listar());
        model.addAttribute("lstProductos", productoService.listar());
        model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
        return "listMovimientos";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute InventarioMovimientoRequestDto movimientoRequestDto,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        try {
            UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
            movimientoRequestDto.setUsuario(principal.getNombreCompleto());

            if (movimientoRequestDto.getFechaMovimiento() == null) {
                movimientoRequestDto.setFechaMovimiento(LocalDateTime.now());
            }

            movimientoService.crear(movimientoRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Movimiento registrado exitosamente");
        } catch (Exception e) {
            log.error("Error al crear movimiento: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/movimientos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        try {
            model.addAttribute("movimientoRequestDto", movimientoService.obtener(id));
            model.addAttribute("lstMovimientos", movimientoService.listarUltimosMovimientos());
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("editando", true);
        } catch (Exception e) {
            log.error("Error al obtener movimiento {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Movimiento no encontrado");
            return "redirect:/movimientos";
        }
        return "listMovimientos";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute InventarioMovimientoRequestDto movimientoRequestDto,
                           RedirectAttributes redirectAttributes) {
        try {
            if (movimientoRequestDto.getId() == null) {
                throw new IllegalArgumentException("El ID del movimiento es obligatorio");
            }
            movimientoService.actualizar(movimientoRequestDto.getId(), movimientoRequestDto);
            redirectAttributes.addFlashAttribute("mensaje", "Movimiento actualizado exitosamente");
        } catch (Exception e) {
            log.error("Error al actualizar movimiento: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/movimientos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        try {
            movimientoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Movimiento eliminado exitosamente (inventario revertido)");
        } catch (Exception e) {
            log.error("Error al eliminar movimiento {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/movimientos";
    }

    @GetMapping("/todos")
    public String listarTodos(Model model) {
        model.addAttribute("lstMovimientos", movimientoService.listar());
        model.addAttribute("lstAlmacenes", almacenService.listar());
        model.addAttribute("lstProductos", productoService.listar());
        model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
        return "listMovimientos";
    }

    @GetMapping("/almacen/{almacenId}")
    public String listarPorAlmacen(@PathVariable Integer almacenId,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("lstMovimientos", movimientoService.listarPorAlmacen(almacenId));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("almacenSeleccionado", almacenId);
            model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
            return "listMovimientos";
        } catch (Exception e) {
            log.error("Error al listar movimientos por almacén {}: {}", almacenId, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/movimientos";
        }
    }

    @GetMapping("/producto/{productoId}")
    public String listarPorProducto(@PathVariable Integer productoId,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("lstMovimientos", movimientoService.listarPorProducto(productoId));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("productoSeleccionado", productoId);
            model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
            return "listMovimientos";
        } catch (Exception e) {
            log.error("Error al listar movimientos por producto {}: {}", productoId, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/movimientos";
        }
    }

    @GetMapping("/tipo/{tipoMovimiento}")
    public String listarPorTipo(@PathVariable String tipoMovimiento,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("lstMovimientos", movimientoService.listarPorTipo(tipoMovimiento));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("tipoSeleccionado", tipoMovimiento);
            model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
            return "listMovimientos";
        } catch (Exception e) {
            log.error("Error al listar movimientos por tipo {}: {}", tipoMovimiento, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/movimientos";
        }
    }

    @GetMapping("/filtrar-fechas")
    public String listarPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime inicio = fechaInicio.atStartOfDay();
            LocalDateTime fin = fechaFin.atTime(LocalTime.MAX);

            model.addAttribute("lstMovimientos", movimientoService.listarPorFechas(inicio, fin));
            model.addAttribute("lstAlmacenes", almacenService.listar());
            model.addAttribute("lstProductos", productoService.listar());
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);
            model.addAttribute("movimientoRequestDto", new InventarioMovimientoRequestDto());
            return "listMovimientos";
        } catch (Exception e) {
            log.error("Error al listar movimientos por fechas: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/movimientos";
        }
    }
}
