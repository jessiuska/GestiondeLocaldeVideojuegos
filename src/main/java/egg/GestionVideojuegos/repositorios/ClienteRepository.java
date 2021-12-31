package egg.GestionVideojuegos.repositorios;

import egg.GestionVideojuegos.entidades.Cliente;
import egg.GestionVideojuegos.modelos.ClienteModel; //Para la query de ranking
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDni(Long dni);

    boolean existsByDni(Long dni);

    @Modifying
    @Query("UPDATE Cliente c SET c.alta = true WHERE c.dni = :dni")
    void habilitar(@Param("dni") Long dni);
    
    List<Cliente> findByAlta(Boolean alta);

    @Query(name = "buscar_top5_clientes", nativeQuery = true)
    List<ClienteModel> buscarTop5();

}
