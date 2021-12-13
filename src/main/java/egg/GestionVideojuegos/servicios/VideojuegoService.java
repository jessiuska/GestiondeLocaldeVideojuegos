package egg.GestionVideojuegos.servicios;

import egg.GestionVideojuegos.entidades.Cliente;
import egg.GestionVideojuegos.entidades.Videojuego;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.repositorios.VideojuegoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideojuegoService {
    @Autowired
    private VideojuegoRepository videoJuegoRepository;

    @Autowired
    private FotoService fotoService;
    
    @Autowired
    private TarjetaService tarjetaService;
    
    @Autowired
    private ClienteService clienteService;
    

    private String mensaje = "No existe ningún videojuego asociado con el ID %s";
    
    @Transactional
    public void crear(Videojuego videoJuego, MultipartFile foto) throws SpringException {
        if (videoJuegoRepository.existsByNombre(videoJuego.getNombre())) {
            throw new SpringException("Ya existe un videojuego registrado con ese nombre");
        }
        Videojuego videojuego = new Videojuego();
        videojuego.setNombre(videoJuego.getNombre());
        videojuego.setPrecioFicha(videoJuego.getPrecioFicha());
        videojuego.setRecaudacion(videoJuego.getRecaudacion());
        if (!foto.isEmpty()) {
            videojuego.setImage(fotoService.copiar(foto));
        }
        videojuego.setAlta(true);
        videoJuegoRepository.save(videojuego);
    }

    @Transactional
    public void modificar(Videojuego videoJuego, MultipartFile foto) throws SpringException {
        Videojuego videojuego = buscarPorId(videoJuego.getId());
                     
        videojuego.setNombre(videoJuego.getNombre());
        videojuego.setPrecioFicha(videoJuego.getPrecioFicha());
        videojuego.setRecaudacion(videoJuego.getRecaudacion());
        if (!foto.isEmpty()) {
            videojuego.setImage(fotoService.copiar(foto));
        }
        videoJuegoRepository.save(videojuego);
    }
    
    @Transactional(readOnly = true)
    public List<Videojuego> buscarTodos() {
        return videoJuegoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Videojuego buscarPorId(Integer id) throws SpringException {
        return videoJuegoRepository.findById(id).orElseThrow(() -> new SpringException(String.format(mensaje, id)));
    }

    @Transactional
    public void habilitar(Integer id) {
        videoJuegoRepository.habilitar(id);
    }

    @Transactional
    public void eliminar(Integer id) {
        videoJuegoRepository.deleteById(id);
    }
    
    public void recaudar(Integer idVideojuego, Double monto) throws SpringException {
            Videojuego videojuego = buscarPorId(idVideojuego);

            //guardo la recaudación actual
            Double recaudacionActual = videojuego.getRecaudacion();

            //actualizo la recaudación
            videojuego.setRecaudacion(recaudacionActual + monto);

            videoJuegoRepository.save(videojuego);
    }
    
    public void jugar(Long dniCliente, Integer idVideojuego) throws SpringException{
        Cliente cliente = clienteService.buscarPorDni(dniCliente);
	Videojuego videojuego = buscarPorId(idVideojuego);

	//averiguo el precio de la ficha del videojuego
	double precioFicha = videojuego.getPrecioFicha();

        //se descuenta de la tarjeta el precioFicha para jugar
	tarjetaService.consumo(cliente.getTarjeta(), precioFicha);
	
	//actualizo la recaudación del videojuego
        recaudar(idVideojuego, precioFicha);
        

    }

    public Double cerrar(Integer idVideojuego) throws SpringException {
            Videojuego videojuego = buscarPorId(idVideojuego);

            //guardo recaudación actual
            Double recaudacion = videojuego.getRecaudacion();

            //pongo la recaudación a 0
            videojuego.setRecaudacion(0.0);

            videoJuegoRepository.save(videojuego);

            return recaudacion;
    }

}
