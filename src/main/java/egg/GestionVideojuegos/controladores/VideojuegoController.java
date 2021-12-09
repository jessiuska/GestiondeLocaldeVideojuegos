package egg.GestionVideojuegos.controladores;

import egg.GestionVideojuegos.entidades.Videojuego;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.servicios.VideojuegoService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/videojuego")
public class VideojuegoController {
    @Autowired
    private VideojuegoService videojuegoService;

    @GetMapping
    public ModelAndView mostrar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("videojuego");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("videojuegos", videojuegoService.buscarTodos());
        return mav;
    }
    
    @GetMapping("/crear")
    public ModelAndView crear(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("videojuego-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("autor", flashMap.get("autor"));
        } else {
            mav.addObject("videojuego", new Videojuego());
        }

        mav.addObject("title", "Crear Videojuego");
        mav.addObject("action", "guardar");
        return mav;
    }
    
    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Integer id, HttpServletRequest request, RedirectAttributes attributes) {
        ModelAndView mav = new ModelAndView("videojuego-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        try {
            if (flashMap != null) {
                mav.addObject("error", flashMap.get("error"));
                mav.addObject("autor", flashMap.get("autor"));
            } else {
                mav.addObject("videojuego", videojuegoService.buscarPorId(id));
            }

            mav.addObject("title", "Editar Videojuego");
            mav.addObject("action", "modificar");
        } catch (SpringException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            mav.setViewName("redirect:/videojuego");
        }

        return mav;
    }
    
    @PostMapping("/guardar")
    public RedirectView guardar(@RequestParam MultipartFile foto, @ModelAttribute Videojuego videoJuego, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/videojuego");

        try {
            videojuegoService.crear(videoJuego, foto);
            attributes.addFlashAttribute("exito", "La creación ha sido realizada satisfactoriamente");
        } catch (SpringException e) {
            attributes.addFlashAttribute("videojuego", videoJuego);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/videojuego/crear");
        }

        return redirectView;
    }

    @PostMapping("/modificar")
    public RedirectView modificar(@RequestParam MultipartFile foto, @ModelAttribute Videojuego videoJuego, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/videojuego");

        try {
            videojuegoService.modificar(videoJuego, foto);
            attributes.addFlashAttribute("exito", "La actualización ha sido realizada satisfactoriamente");
        } catch (SpringException e) {
            attributes.addFlashAttribute("videojuego", videoJuego);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/videojuego/editar/" + videoJuego.getId());
        }

        return redirectView;
    }

    @PostMapping("/habilitar/{id}")
    public RedirectView habilitar(@PathVariable Integer id) {
        videojuegoService.habilitar(id);
        return new RedirectView("/videojuego");
    }

    @PostMapping("/eliminar/{id}")
    public RedirectView eliminar(@PathVariable Integer id) {
        videojuegoService.eliminar(id);
        return new RedirectView("/videojuego");
    }
    
//    @PostMapping("/recaudacion")
//    public RedirectView recaudacionDiaria() {
//        videojuegoService.recaudacionDiaria();
//        return new RedirectView("/recaudacion-diaria");
//    }

}
