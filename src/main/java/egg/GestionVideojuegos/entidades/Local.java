package egg.GestionVideojuegos.entidades;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Setter
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    @CreatedDate
    private LocalDate fecAlta;
    @LastModifiedDate
    private LocalDate fecBaja;
    @Column(nullable = false)
    private Double recaudacion;
    
    @OneToMany(mappedBy = "local", fetch = FetchType.EAGER)
    private List<Empleado> empleado;
    @OneToMany(mappedBy = "local", fetch = FetchType.EAGER)
    private List<Cliente> cliente;
    @OneToMany(mappedBy = "local", fetch = FetchType.EAGER)
    private List<Videojuego> videojuego;
    
}
