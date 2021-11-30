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
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE tarjeta SET alta = false WHERE id = ?")
public class Tarjeta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Double saldo;
    
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime fechaAlta;

    @LastModifiedDate    
    @Column(nullable = false)
    private LocalDateTime fechaModificacion;
    
    private Boolean alta;
}
