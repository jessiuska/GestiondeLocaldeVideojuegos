package egg.GestionVideojuegos.repositorios;

import egg.GestionVideojuegos.entidades.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalRepository extends JpaRepository<Local, Integer> {

    @Query("SELECT SUM(v.recaudacion) FROM Videojuego v")
    double cierreDeCaja();

}
