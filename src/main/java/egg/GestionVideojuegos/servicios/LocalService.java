package egg.GestionVideojuegos.servicios;

import egg.GestionVideojuegos.entidades.Local;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.repositorios.LocalRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalService {

    @Autowired
    private LocalRepository localRepository;
    
    /*@Autowired
    private TransaccionService transaccionService;*/

    private String mensaje = "No existe ningún local asociado con el ID %s";

    @Transactional
    public void crear() {
        Local local = new Local();
        local.setNombre("BasicGames");
        local.setRecaudacion(0.0);
        local.setPrecioTarjeta(100.0);
        local.setFechaUltimoCierre(local.getFechaAlta());

        localRepository.save(local);

    }

    @Transactional
    public void modificar(Local dto) throws SpringException {
        Local local = localRepository.findById(dto.getId()).orElseThrow(() -> new SpringException(String.format(mensaje, dto.getId())));
        local.setNombre(dto.getNombre());
        local.setPrecioTarjeta(dto.getPrecioTarjeta());

        localRepository.save(local);
    }
    
    @Transactional
    public void recaudar(Local dto) throws SpringException {
        LocalDateTime ahora = LocalDateTime.now();

        Local local = localRepository.findById(dto.getId()).orElseThrow(() -> new SpringException(String.format(mensaje, dto.getId())));
        local.setRecaudacion(localRepository.cierreDeCaja());
        local.setFechaUltimoCierre(ahora);
        
        localRepository.save(local);
    } 
    
    
    
    
}
