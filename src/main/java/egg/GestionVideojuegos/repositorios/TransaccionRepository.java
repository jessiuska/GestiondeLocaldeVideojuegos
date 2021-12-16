package egg.GestionVideojuegos.repositorios;

import egg.GestionVideojuegos.entidades.Transaccion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {

    Optional<Transaccion> findById(Integer id);

    boolean existsById(Integer id);
    
    List<Transaccion> findBytipoTransaccion(Integer tipoTransaccion);
    
}
