package egg.GestionVideojuegos.controladores;

import egg.GestionVideojuegos.entidades.Cliente;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.servicios.ClienteService;
import egg.GestionVideojuegos.servicios.LocalService;
import egg.GestionVideojuegos.servicios.TransaccionService;
import egg.GestionVideojuegos.servicios.VideojuegoService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller

@RequestMapping("/local")
public class LocalController {

    @Autowired
    private LocalService localService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VideojuegoService videojuegoService;

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping("/carga")
    public ModelAndView cargaSaldo(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("carga-saldo");

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("title", "Carga de saldo");
        mav.addObject("clientes", clienteService.buscarTodos());
        return mav;

    }

    @PostMapping("/cargar")
    public RedirectView cargar(@ModelAttribute Cliente cliente, @RequestParam("monto") Double monto, RedirectAttributes attributes) {
        RedirectView redirectView = new RedirectView("/cliente");

        try {
            localService.cargarTarjeta(cliente, monto);
            attributes.addFlashAttribute("exito", "La carga de $" + monto + " ha sido exitosa");
        } catch (SpringException e) {
            attributes.addFlashAttribute("cliente", cliente);
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/local/carga");
        }

        return redirectView;
    }

    @GetMapping("/cierres")
    public ModelAndView consultaCierres(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("cierres");

        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("title", "Recaudaciones");
        mav.addObject("cierres", transaccionService.buscarCierres());

        return mav;
    }
    
    @PostMapping("/cierre")
    public RedirectView cierreCaja(HttpSession session, RedirectAttributes attributes) {
        try {
            int idEmpleado = Integer.valueOf(session.getId()); //Es un string
            localService.cerrarCaja(idEmpleado);
            attributes.addFlashAttribute("exito", "El cierre de caja ha sido realizado correctamente.");
        } catch (SpringException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/home");

    }

    @PostMapping("/cambiar-tarjeta")
    public RedirectView cambiarTarjeta(@ModelAttribute Cliente cliente, RedirectAttributes attributes) {
        try {
            clienteService.cambiarTarjeta(cliente);
            attributes.addFlashAttribute("exito", "El cambio de tarjeta ha sido realizado correctamente.");
        } catch (SpringException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/home");
    }

}
