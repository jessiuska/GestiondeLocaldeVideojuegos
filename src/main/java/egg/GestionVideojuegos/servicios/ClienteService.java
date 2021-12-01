package egg.GestionVideojuegos.servicios;

import egg.GestionVideojuegos.entidades.Cliente;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.enums.Rol;
import egg.GestionVideojuegos.repositorios.ClienteRepository;

import java.util.Collections;
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
    private EmailService emailService;
    
    private String mensaje = "No existe ningún cliente asociado con el ID %s";

    @Transactional
    public void crear(Cliente dto) throws SpringException {
        if (clienteRepository.existsByDni(dto.getDni())) {
            throw new SpringException("Ya existe un cliente con este nombre de usuario");
        }

        Cliente cliente = new Cliente();

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setDni(dto.getDni());
        cliente.setTarjeta(dto.getTarjeta());
        
        if (clienteRepository.findAll().isEmpty()) {
            cliente.setRol(Rol.USER);
        } else if (dto.getRol() == null) {
            cliente.setRol(Rol.USER);
        } else {
            cliente.setRol(dto.getRol());
        }
        cliente.setAlta(true);
        //emailService.enviarThread(dto.getCorreo());
        clienteRepository.save(cliente);
    }

    @Transactional
    public void modificar(Cliente dto) throws SpringException {
        Cliente cliente = clienteRepository.findByDni(dto.getDni()).orElseThrow(() ->
                new SpringException(String.format(mensaje, dto.getDni())));
        
        cliente.setDni(dto.getDni());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setTarjeta(dto.getTarjeta());
        cliente.setRol(dto.getRol());
        clienteRepository.save(cliente);
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorDni(Integer dni) throws SpringException {
        return clienteRepository.findByDni(dni).orElseThrow(() ->
                new SpringException(String.format(mensaje, dni)));
    }

    @Transactional
    public void habilitar(Integer dni) {
        clienteRepository.habilitar(dni);
    }

    @Transactional
    public void eliminar(Integer dni) {
        clienteRepository.deleteById(dni);
    }
}
