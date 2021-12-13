package egg.GestionVideojuegos.repositorios;

import egg.GestionVideojuegos.entidades.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Integer>{
    
    boolean existsByNombre(String nombre);

    @Modifying
    @Query("UPDATE Videojuego v SET v.alta = true WHERE v.id = :id")
    void habilitar(@Param("id") Integer id);
    
   
    
    
}