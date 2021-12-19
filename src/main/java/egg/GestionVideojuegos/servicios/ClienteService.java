package egg.GestionVideojuegos.servicios;

import egg.GestionVideojuegos.entidades.Cliente;
import egg.GestionVideojuegos.entidades.Tarjeta;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.enums.Rol;
import egg.GestionVideojuegos.repositorios.ClienteRepository;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TarjetaService tarjetaService;

    @Autowired
    private EmailService emailService;

    private String mensaje = "No existe ningún cliente asociado con el DNI %s";

    @Transactional
    public void crear(Cliente dto) throws SpringException {
        if (clienteRepository.existsByDni(dto.getDni())) {
            throw new SpringException("Ya existe un cliente con este nombre de usuario");
        }

        Cliente cliente = new Cliente();

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setDni(dto.getDni());
        cliente.setTarjeta(tarjetaService.crear());
        cliente.setRol(Rol.USER);
        cliente.setAlta(true);

        clienteRepository.save(cliente);
    }

    @Transactional
    public void modificar(Cliente dto) throws SpringException {
        Cliente cliente = clienteRepository.findByDni(dto.getDni()).orElseThrow(()
                -> new SpringException(String.format(mensaje, dto.getDni())));

        cliente.setDni(dto.getDni());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setTarjeta(dto.getTarjeta());
        //cliente.setRol(dto.getRol());
        cliente.setRol(Rol.USER); //por las dudas, ya que siempre va a ser USER
        clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarTodos() {
        return clienteRepository.findByAlta(true);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorDni(Long dni) throws SpringException {
        return clienteRepository.findByDni(dni).orElseThrow(()
                -> new SpringException(String.format(mensaje, dni)));
    }

    @Transactional
    public void habilitar(Long dni) {
        clienteRepository.habilitar(dni);
    }

    @Transactional
    public void eliminar(Long dni) {
        clienteRepository.deleteById(dni);
    }

    @Transactional
    public void cambiarTarjeta(Long dniCliente) throws SpringException {
        Cliente cliente = buscarPorDni(dniCliente);

        //guardo el saldo de la tarjeta actual
        Double tempSaldo = cliente.getTarjeta().getSaldo();

        //elimino la tarjeta
        tarjetaService.eliminar(cliente.getTarjeta().getId());
        
        /*
        //creo una nueva tarjeta temporal con el saldo anterior
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setSaldo(tempSaldo);

        //le asigno la nueva tarjeta al cliente
        cliente.setTarjeta(tarjeta);
        */
        
        //Le creo una nueva tarjeta (saldo 0.0)
        cliente.setTarjeta(tarjetaService.crear());
        //Le modifico el saldo
        cliente.getTarjeta().setSaldo(tempSaldo);

        //guardo el cliente
        clienteRepository.save(cliente);

    }
}
