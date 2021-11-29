
package egg.GestionVideojuegos.entidades;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Videojuego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nombre;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate fecAlta;
    
    @LastModifiedDate
    private LocalDate fecBaja;
    
    @Column(nullable = false)
    private Double precioFicha;
    
    @Column(nullable = false)
    private Double recaudacion;
    
}
