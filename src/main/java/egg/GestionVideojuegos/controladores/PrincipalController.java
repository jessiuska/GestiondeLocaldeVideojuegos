package egg.GestionVideojuegos.controladores;

import egg.GestionVideojuegos.entidades.Empleado;
import egg.GestionVideojuegos.enums.Rol;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.servicios.EmpleadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Controller
public class PrincipalController {
    
    @Value("${custom.nombre-sistema}")
    private String nombreSistema;
    
    @Autowired
    private EmpleadoService empleadoService;

    //TEST
    @GetMapping("/")
    public RedirectView preInicio() {
        return new RedirectView("/home");
    }
    //TEST
    
    @GetMapping("/home")
    public ModelAndView inicio() {
        //return new ModelAndView("index");
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("title", nombreSistema);
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, Principal principal) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("title", nombreSistema + " - Login");

        if (error != null) {
            mav.addObject("error", "Usuario o contraseña incorrectos");
        }

        if (logout != null) {
            mav.addObject("logout", "Ha salido correctamente de la plataforma");
        }

        if (principal != null) {
            mav.setViewName("redirect:/home");
        }

        return mav;
    }

    @GetMapping("/signup")
    public ModelAndView signup(HttpServletRequest request, Principal principal) {
        ModelAndView mav = new ModelAndView("signup");
        mav.addObject("title", nombreSistema + " - Registro");
        
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (principal != null) {
            mav.setViewName("redirect:/home");
        }

        if (flashMap != null) {
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("empleado", flashMap.get("empleado"));
        } else {
            //mav.addObject("empleado", new Empleado());
            Empleado empleado  = new Empleado();
            empleado.setRol(Rol.USER);
            mav.addObject("empleado", empleado);
        }

        return mav;
    }

    @PostMapping("/registro")
    public RedirectView signup(@ModelAttribute Empleado empleado, HttpServletRequest request, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/login");

        try {
            empleadoService.crear(empleado);
            request.login(empleado.getUsuario(), empleado.getClave());
        } catch (SpringException e) {
            attributes.addFlashAttribute("empleado", empleado);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/signup");
        } catch (ServletException e) {
            attributes.addFlashAttribute("error", "Error al realizar auto-login");
        }

        return redirectView;
    }
}
