package egg.GestionVideojuegos.servicios;

import egg.GestionVideojuegos.entidades.Cliente;
import egg.GestionVideojuegos.entidades.Local;
import egg.GestionVideojuegos.entidades.Videojuego;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.repositorios.LocalRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalService {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private VideojuegoService videojuegoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private TarjetaService tarjetaService;

    @Autowired
    private TransaccionService transaccionService;
    
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
    public void cerrarCaja(int idEmpleado) throws SpringException {
        Local local = localRepository.findById(0).orElseThrow(() -> new SpringException(String.format(mensaje, 0)));

        LocalDateTime desde = local.getFechaUltimoCierre(); //la fecha del último cierre va a pasar a ser la anterior para el nuevo cierre
        LocalDateTime ahora = LocalDateTime.now();
        
        Double totalRecaudacion = 0.0;

        List<Videojuego> videojuegos = videojuegoService.buscarTodos();

        for (Videojuego videojuego : videojuegos) {
            totalRecaudacion += videojuegoService.cerrar(videojuego.getId());
        }

        local.setRecaudacion(totalRecaudacion);
        local.setFechaUltimoCierre(ahora); //ahora es la fecha del último cierre

        localRepository.save(local);

        //tipo, monto, dnicliente, idempleado, idvideojuego, fechadesde, fechahasta
        transaccionService.crearTransaccion(4, totalRecaudacion, null, idEmpleado, null, desde, ahora);
    }

    @Transactional
    public void cargarTarjeta(Cliente dto, Double monto) throws SpringException {
        tarjetaService.carga(dto.getTarjeta(), monto);
        transaccionService.crearTransaccion(1, monto, dto.getDni(),0 , null, null, null);
    }

    public void aumentarFicha(Double porcentaje) throws SpringException {
        List<Videojuego> videojuegos = videojuegoService.buscarTodos();

        for (Videojuego videojuego : videojuegos) {
            double nuevoPrecio = videojuego.getPrecioFicha() + (videojuego.getPrecioFicha() * porcentaje) / 100;
            videojuegoService.nuevoPrecioFicha(videojuego.getId(), nuevoPrecio);
        }
    }

    public void rebajarFicha(Double porcentaje) throws SpringException {
        List<Videojuego> videojuegos = videojuegoService.buscarTodos();

        for (Videojuego videojuego : videojuegos) {
            double nuevoPrecio = videojuego.getPrecioFicha() - (videojuego.getPrecioFicha() * porcentaje) / 100;
            videojuegoService.nuevoPrecioFicha(videojuego.getId(), nuevoPrecio);
        }
    }

    public void simularJuegos(Cliente dto, int repetir) throws SpringException {
        int v, c;
        
        List<Cliente> clientes = clienteService.buscarTodos();
        List<Videojuego> videojuegos = videojuegoService.buscarTodos();
        
        for (int i = 0; i <= (repetir - 1); i++) {
            v = (int) (Math.random() * videojuegos.size() + 1);
            c = (int) (Math.random() * clientes.size() + 1);
            
            if (dto.getTarjeta().getSaldo() >= videojuegos.get(v).getPrecioFicha()){
                videojuegoService.jugar(clientes.get(c).getDni(), videojuegos.get(v).getId());
            }
        }
    }
    
}
