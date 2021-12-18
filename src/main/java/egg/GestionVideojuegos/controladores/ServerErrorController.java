package egg.GestionVideojuegos.controladores;

import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ServerErrorController implements ErrorController {

    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView handleError(HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("error");

        int errorCode = response.getStatus();
        String errorMsg = "";
        
        switch (errorCode) {
            case 400:
                errorMsg = "Bad Request";
                break;
            case 401:
                errorMsg = "Unauthorized";
                break;
            case 403:
                errorMsg = "Forbidden";
                break;
            case 404:
                errorMsg = "Not Found";
                break;
            case 500:
                errorMsg = "Internal Server Error";
                break;
            default:
                errorMsg = "OOOPS!";
        }
        
        mav.addObject("title", "Error :(");
        mav.addObject("codigo", errorCode);
        mav.addObject("tipo", errorMsg);
        
        return mav;
    }

}
