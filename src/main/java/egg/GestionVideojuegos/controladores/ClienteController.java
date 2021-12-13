package egg.GestionVideojuegos.controladores;

import egg.GestionVideojuegos.entidades.Cliente;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.servicios.ClienteService;
import egg.GestionVideojuegos.servicios.TarjetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
// @PreAuthorize("hasRole('USER')")
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    //@PreAuthorize("hasAnyRole('USER')")
    public ModelAndView mostrar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("clientes");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("clientes", clienteService.buscarTodos());
        return mav;
    }

    @GetMapping("/crear")
    //@PreAuthorize("hasRole('USER')")
    public ModelAndView crear(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("cliente-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("cliente", flashMap.get("cliente"));
        } else {
            mav.addObject("cliente", new Cliente());
        }

        mav.addObject("title", "Crear Cliente");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{dni}")
    public ModelAndView editar(@PathVariable Long dni, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {
        if (!session.getAttribute("dni").equals(dni)) {
            return new ModelAndView(new RedirectView("/home"));
        }

        ModelAndView mav = new ModelAndView("cliente-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        try {
            if (flashMap != null) {
                mav.addObject("error", flashMap.get("error"));
                mav.addObject("cliente", flashMap.get("cliente"));
            } else {
                mav.addObject("cliente", clienteService.buscarPorDni(dni));
            }

            mav.addObject("title", "Editar Cliente");
            mav.addObject("action", "modificar");
        } catch (SpringException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            mav.setViewName("redirect:/cliente");
        }

        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@ModelAttribute Cliente cliente, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/cliente");

        try {
            clienteService.crear(cliente);
            attributes.addFlashAttribute("exito", "La creación ha sido realizada satisfactoriamente");
        } catch (SpringException e) {
            attributes.addFlashAttribute("cliente", cliente);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/cliente/crear");
        }

        return redirectView;
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@ModelAttribute Cliente cliente, HttpSession session, RedirectAttributes attributes) {
        if (!session.getAttribute("id").equals(cliente.getDni())) {
            return new RedirectView("/home");
        }

        RedirectView redirectView = new RedirectView("/cliente");

        try {
            clienteService.modificar(cliente);
            attributes.addFlashAttribute("exito", "La actualización ha sido realizada satisfactoriamente");
        } catch (SpringException e) {
            attributes.addFlashAttribute("cliente", cliente);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/cliente/editar/" + cliente.getDni());
        }

        return redirectView;
    }

    @PostMapping("/habilitar/{dni}")
    public RedirectView habilitar(@PathVariable Long dni) {
        clienteService.habilitar(dni);
        return new RedirectView("/cliente");
    }

    @PostMapping("/eliminar/{dni}")
    public RedirectView eliminar(@PathVariable Long dni) {
        clienteService.eliminar(dni);
        return new RedirectView("/cliente");
    }

    @PostMapping("/cambiar-tarjeta")
    public RedirectView cambiarTarjeta(@ModelAttribute Cliente cliente, HttpSession session, RedirectAttributes attributes) {
        try {
            clienteService.cambiarTarjeta(cliente);
        } catch (SpringException e) {
            attributes.addFlashAttribute("cliente", cliente);
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/cliente");
    }
}
