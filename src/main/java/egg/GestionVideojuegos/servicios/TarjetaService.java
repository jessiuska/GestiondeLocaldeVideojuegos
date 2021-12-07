package egg.GestionVideojuegos.servicios;

import egg.GestionVideojuegos.entidades.Tarjeta;
import egg.GestionVideojuegos.excepciones.SpringException;
import egg.GestionVideojuegos.repositorios.TarjetaRepository;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    private String mensaje = "No existe ningúna tarjeta asociado con el ID %s";

    @Transactional
    public void crear(Tarjeta dto) throws SpringException {
        if (tarjetaRepository.existsById(dto.getId())) {
            throw new SpringException("Ya existe una Tarjeta con este numero de ID");
        }

        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setSaldo(0.0);
        tarjeta.setAlta(true);

        tarjetaRepository.save(tarjeta);
    }

    @Transactional
    // precioficha
    public void consumo(Tarjeta dto) throws SpringException {
        Tarjeta tarjeta = tarjetaRepository.findById(dto.getId()).orElseThrow(()
                -> new SpringException(String.format(mensaje, dto.getId())));

        tarjeta.setSaldo(dto.getSaldo());
        tarjetaRepository.save(tarjeta);
    }

    @Transactional
    public void carga (Tarjeta dto, Double saldo) throws SpringException {
        Tarjeta tarjeta = tarjetaRepository.findById(dto.getId()).orElseThrow(()
                -> new SpringException(String.format(mensaje, dto.getId())));

        tarjeta.setSaldo(dto.getSaldo()+saldo);
        
        tarjetaRepository.save(tarjeta);
    }

    @Transactional(readOnly = true)
    public List<Tarjeta> buscarTodas() {
        return tarjetaRepository.findAll();
    }

// buscar por DNI del cliente
    @Transactional(readOnly = true)
    public Tarjeta buscarPorId(Integer id) throws SpringException {
        return tarjetaRepository.findById(id).orElseThrow(()
                -> new SpringException(String.format(mensaje, id)));
    }

    @Transactional
    public void habilitar(Integer id) {
        tarjetaRepository.habilitar(id);
    }

    @Transactional
    public void eliminar(Integer id) {
        tarjetaRepository.deleteById(id);
    }
}
