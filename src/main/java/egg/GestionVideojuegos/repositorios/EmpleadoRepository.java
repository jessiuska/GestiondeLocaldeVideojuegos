package egg.GestionVideojuegos.repositorios;

import egg.GestionVideojuegos.entidades.Empleado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    Optional<Empleado> findByUsuario(String usuario);

    boolean existsByUsuario(String usuario);

    @Modifying
    @Query("UPDATE Empleado e SET e.alta = true WHERE e.id = :id")
    void habilitar(@Param("id") Integer id);
    
    List<Empleado> findByAlta(Boolean alta);
}
