package egg.GestionVideojuegos.entidades;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Local {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nombre;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAlta;
    
    private LocalDateTime fechaUltimoCierre;
    
    private Double precioTarjeta;
    
    @Column(nullable = false)
    private Double recaudacion;

}
