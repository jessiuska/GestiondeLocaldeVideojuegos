package egg.GestionVideojuegos.controladores;

import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.servicios.ClienteService;
import egg.GestionVideojuegos.servicios.LocalService;
import egg.GestionVideojuegos.servicios.TransaccionService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public RedirectView cargar(HttpSession session, @RequestParam("dnicliente") Long dnicliente, @RequestParam("monto") Double monto, RedirectAttributes attributes) {
        //RedirectView redirectView = new RedirectView("/cliente");
        try {
            int idEmpleado = (int) session.getAttribute("id"); //Es un string
            //COMENTAR ESTO, SOLO TEST
            /*
            session = null;
            int idEmpleado = 666;
            if (session != null) {
                idEmpleado = Integer.valueOf(session.getId());
            }
            */

            localService.cargarTarjeta(dnicliente, monto, idEmpleado);
            attributes.addFlashAttribute("exito", "La carga de $" + monto + " ha sido exitosa");
            System.out.println("DEBUG> Cliente DNI " + dnicliente + " carga $" + monto);
        } catch (SpringException e) {
            //attributes.addFlashAttribute("cliente", cliente);
            attributes.addFlashAttribute("error", e.getMessage());
            //redirectView.setUrl("/local/carga");
        }

        return new RedirectView("/local/carga");
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

    @GetMapping("/cierre")
    public RedirectView cierreCaja(HttpSession session, RedirectAttributes attributes) {
        try {
            int idEmpleado = (int) session.getAttribute("id"); //Es un string
            //COMENTAR ESTO, SOLO TEST
            /*
            session = null;
            int idEmpleado = 666;
            if (session != null) {
                idEmpleado = Integer.valueOf(session.getId());
            }*/

            localService.cerrarCaja(idEmpleado);
            attributes.addFlashAttribute("exito", "El cierre de caja ha sido realizado correctamente.");
        } catch (SpringException e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/local/cierres");

    }

}
