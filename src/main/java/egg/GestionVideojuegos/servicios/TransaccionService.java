package egg.GestionVideojuegos.servicios;

import egg.GestionVideojuegos.entidades.Transaccion;
import egg.GestionVideojuegos.repositorios.TransaccionRepository;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransaccionService {
    
    
    @Autowired
    private TransaccionRepository transaccionRepository;
        
    @Transactional
    public void crearTransaccion(Integer codigoOperacion, Double monto, Long dniCliente, Integer idEmpleado, Integer idVideojuego, LocalDate fechaDesde, LocalDate fechaHasta){
    
        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(codigoOperacion);
        transaccion.setMonto(monto);
        
        if(dniCliente != null){
            transaccion.setDniCliente(dniCliente);
        }
        if(idEmpleado != null) {
            transaccion.setIdEmpleado(idEmpleado);
        }
        if(idVideojuego !=null) {
            transaccion.setIdVideojuego(idVideojuego);
        }
        if(fechaDesde != null) {
            transaccion.setFechaDesde(fechaDesde);
        }
        if(fechaHasta != null) {
            transaccion.setFechaHasta(fechaHasta);
        }
        
        transaccionRepository.save(transaccion);
    } 
    
}
