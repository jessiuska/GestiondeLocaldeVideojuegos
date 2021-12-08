package egg.GestionVideojuegos.entidades;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
    
    
    /*@OneToMany(mappedBy = "local", fetch = FetchType.EAGER)
    private List<Empleado> empleados;
    @OneToMany(mappedBy = "local", fetch = FetchType.EAGER)
    private List<Cliente> clientes;
    @OneToMany(mappedBy = "local", fetch = FetchType.EAGER)
    private List<Videojuego> videojuegos;*/
    
}
