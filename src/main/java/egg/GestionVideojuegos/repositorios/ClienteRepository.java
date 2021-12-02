package egg.GestionVideojuegos.repositorios;

import egg.GestionVideojuegos.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByDni(Integer dni);

    boolean existsByDni(Integer dni);

    @Modifying
    @Query("UPDATE Cliente c SET c.alta = true WHERE c.dni = :dni")
    void habilitar(@Param("dni") Integer dni);
}
