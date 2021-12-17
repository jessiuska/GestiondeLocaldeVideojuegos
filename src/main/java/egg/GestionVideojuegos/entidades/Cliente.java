package egg.GestionVideojuegos.entidades;

import egg.GestionVideojuegos.enums.Rol;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE cliente SET alta = false WHERE dni = ?")
public class Cliente {

    @Id
    private Long dni;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellido;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAlta;
    
    @LastModifiedDate
    private LocalDateTime fechaModificacion;

    @OneToOne
    private Tarjeta tarjeta;

    @Enumerated(EnumType.STRING)
    //@Column(nullable = false)
    private Rol rol;
    
    private Boolean alta;
    
}
