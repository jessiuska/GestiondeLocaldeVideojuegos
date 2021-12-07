package egg.GestionVideojuegos.repositorios;

import egg.GestionVideojuegos.entidades.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Integer> {

    Optional<Tarjeta> findById(Integer id);

    boolean existsById(Integer id);

    @Modifying
    @Query("UPDATE Tarjeta t SET t.alta = true WHERE t.id = :id")
    void habilitar(@Param("id") Integer id);
}
