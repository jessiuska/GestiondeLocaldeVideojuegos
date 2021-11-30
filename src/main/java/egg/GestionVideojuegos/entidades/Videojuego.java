package egg.GestionVideojuegos.entidades;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE videojuego SET alta = false WHERE id = ?")
public class Videojuego {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private Double precioFicha;
    
    @Column(nullable = false)
    private Double recaudacion;

    private String image;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaAlta;
    
    @LastModifiedDate
    private LocalDateTime fechaModificacion;
    
    private Boolean alta;
    
}
