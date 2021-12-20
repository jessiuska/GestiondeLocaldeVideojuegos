package egg.GestionVideojuegos.servicios;

import egg.GestionVideojuegos.entidades.Empleado;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.enums.Rol;
import egg.GestionVideojuegos.repositorios.EmpleadoRepository;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class EmpleadoService implements UserDetailsService {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder encoder;
    
    private String mensaje = "No existe ningún usuario asociado con el ID %s";

    @Transactional
    public void crear(Empleado dto) throws SpringException {
        if (empleadoRepository.existsByUsuario(dto.getUsuario())) {
            throw new SpringException("Ya existe un empleado con este nombre de usuario");
        }

        Empleado empleado = new Empleado();

        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setUsuario(dto.getUsuario());
        empleado.setClave(encoder.encode(dto.getClave()));
        
        if (empleadoRepository.findAll().isEmpty()) {
            empleado.setRol(Rol.ADMIN);
        /*} else if (dto.getRol() == null) {
            empleado.setRol(Rol.CAJERO);*/
        } else {
            empleado.setRol(dto.getRol());
        }
        empleado.setAlta(true);
        //emailService.enviarThread(dto.getCorreo());
        empleadoRepository.save(empleado);
    }

    @Transactional
    public void modificar(Empleado dto, HttpSession session) throws SpringException {
        /*
        if (empleadoRepository.existsByUsuario(dto.getUsuario()) &&
                !session.getAttribute("usuario").equals(dto.getUsuario()))
            throw new SpringException("Ya existe un usuario con ese nombre de usuario");
        */
        
        Empleado empleado = buscarPorId(dto.getId());
        
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setUsuario(dto.getUsuario());
        
        //Si la clave NO viene vacía, la cambia, sino deja la misma
        if (!dto.getClave().isEmpty()) empleado.setClave(encoder.encode(dto.getClave()));
        //Si se modifica un rol se guarda, si no se tocó va a llegar null así que se deja el que estaba
        if (dto.getRol() != null) empleado.setRol(dto.getRol());
        
        empleadoRepository.save(empleado);
        
        //Actualiza datos de la sesión solo si se modifica el usuario que está en la sesión
        if (session.getAttribute("id").equals(empleado.getId())) {
            session.setAttribute("nombre", dto.getNombre());
            session.setAttribute("apellido", dto.getApellido());
            session.setAttribute("usuario", dto.getUsuario());
            session.setAttribute("rol", dto.getRol().name());
        }
    }
    
    @Transactional(readOnly = true)
    public List<Empleado> buscarTodos() {
        return empleadoRepository.findByAlta(true);
    }

    @Transactional(readOnly = true)
    public Empleado buscarPorId(Integer id) throws SpringException {
        return empleadoRepository.findById(id).orElseThrow(() ->
                new SpringException(String.format(mensaje, id)));
    }

    @Transactional
    public void habilitar(Integer id) {
        empleadoRepository.habilitar(id);
    }

    @Transactional
    public void eliminar(Integer id) {
        empleadoRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        Empleado empleado = empleadoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario: " + usuario));
        
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + empleado.getRol().name());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);

        session.setAttribute("id", empleado.getId());
        session.setAttribute("usuario", empleado.getUsuario());
        session.setAttribute("nombre", empleado.getNombre());
        session.setAttribute("apellido", empleado.getApellido());
        session.setAttribute("rol", empleado.getRol().name());

        return new User(empleado.getUsuario(), empleado.getClave(), Collections.singletonList(authority));
    }

}
