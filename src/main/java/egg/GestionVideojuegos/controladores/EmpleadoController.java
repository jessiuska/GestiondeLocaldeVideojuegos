package egg.GestionVideojuegos.controladores;

import egg.GestionVideojuegos.entidades.Empleado;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.servicios.EmpleadoService;
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
// @PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    //@PreAuthorize("hasAnyRole('ADMIN', 'CAJERO')")
    public ModelAndView mostrar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("empleado");
        mav.addObject("title", "Listado de Empleados");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("title", "Listado de empleados");
        mav.addObject("empleados", empleadoService.buscarTodos());
        return mav;
    }

    @GetMapping("/crear")
    //@PreAuthorize("hasRole('ADMIN')")
    public ModelAndView crear(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("empleado-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("empleado", flashMap.get("empleado"));
        } else {
            mav.addObject("empleado", new Empleado());
        }

        mav.addObject("title", "Crear Empleado");
        mav.addObject("action", "guardar");
        return mav;
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Integer id, HttpServletRequest request, HttpSession session, RedirectAttributes attributes) {
        ModelAndView mav = new ModelAndView("empleado-formulario");

        if (!session.getAttribute("id").equals(id) && !session.getAttribute("rol").equals("ADMIN")) {
            attributes.addFlashAttribute("error", "No puede editar un usuario que no es el suyo");
            mav.setViewName("redirect:/empleado");
            //return new ModelAndView(new RedirectView("/empleado"));
        }

        //ModelAndView mav = new ModelAndView("empleado-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        try {
            if (flashMap != null) {
                mav.addObject("error", flashMap.get("error"));
                mav.addObject("empleado", flashMap.get("empleado"));
            } else {
                mav.addObject("empleado", empleadoService.buscarPorId(id));
            }

            mav.addObject("title", "Editar Empleado");
            mav.addObject("action", "modificar");
        } catch (SpringException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            mav.setViewName("redirect:/empleado");
        }

        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardar(@ModelAttribute Empleado empleado, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/empleado");

        try {
            empleadoService.crear(empleado);
            attributes.addFlashAttribute("exito", "La creación ha sido realizada satisfactoriamente");
        } catch (SpringException e) {
            attributes.addFlashAttribute("empleado", empleado);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/empleado/crear");
        }

        return redirectView;
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@ModelAttribute Empleado empleado, HttpSession session, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/empleado");

        try {
            empleadoService.modificar(empleado, session);
            if (session.getAttribute("id").equals(empleado.getId())) {
                //empleadoService.actualizarSesion(empleado, session);
            }
            attributes.addFlashAttribute("exito", "La actualización ha sido realizada satisfactoriamente");
        } catch (SpringException e) {
            attributes.addFlashAttribute("empleado", empleado);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/empleado/editar/" + empleado.getId());
        }

        return redirectView;
    }

    @PostMapping("/habilitar/{id}")
    public RedirectView habilitar(@PathVariable Integer id) {
        empleadoService.habilitar(id);
        return new RedirectView("/empleado");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable Integer id) {
        empleadoService.eliminar(id);
        return new RedirectView("/empleado");
    }
}
