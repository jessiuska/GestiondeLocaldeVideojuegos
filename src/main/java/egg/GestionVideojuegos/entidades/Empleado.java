package egg.GestionVideojuegos.entidades;

import egg.GestionVideojuegos.enums.Rol;
import java.time.LocalDateTime;
import javax.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE empleado SET alta = false WHERE id = ?")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String usuario;

    @Column(nullable = false)
    private String clave;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAlta;

    @LastModifiedDate
    private LocalDateTime fechaModificacion;
    
    private Boolean alta;
    
}
