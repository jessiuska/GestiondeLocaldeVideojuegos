package egg.GestionVideojuegos.controladores;

import egg.GestionVideojuegos.entidades.Cliente;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.servicios.ClienteService;
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
@PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    //@PreAuthorize("hasAnyRole('USER')")
    public ModelAndView mostrar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("cliente");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("title", "Listado de clientes");
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

    @PostMapping("/guardar")
    public RedirectView guardar(@ModelAttribute Cliente cliente, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/cliente");

        try {
            clienteService.crear(cliente);
            attributes.addFlashAttribute("exito", "El cliente se creó con éxito");
        } catch (SpringException e) {
            attributes.addFlashAttribute("cliente", cliente);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/cliente");
        }

        return redirectView;
    }

    @GetMapping("/editar/{dni}")
    public ModelAndView editar(@PathVariable Long dni, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {
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

    @PostMapping("/modificar")
    public RedirectView modificar(@ModelAttribute Cliente cliente, HttpSession session, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/cliente");

        try {
            clienteService.modificar(cliente);
            attributes.addFlashAttribute("exito", "El cliente se modificó correctamente");
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

    @PostMapping("/cambiar-tarjeta/{dni}")
    public RedirectView cambiarTarjeta(HttpSession session, @PathVariable Long dni, RedirectAttributes attributes) {
        try {
            clienteService.cambiarTarjeta(dni);
            attributes.addFlashAttribute("exito", "El cambio de tarjeta se realizó correctamente");
            //System.out.println("Se cambia tarjeta con ID=" + cliente.getTarjeta().getId() + " para cliente DNI=" + cliente.getDni());
        } catch (SpringException e) {
            //attributes.addFlashAttribute("cliente", cliente);
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/cliente");
    }

}
