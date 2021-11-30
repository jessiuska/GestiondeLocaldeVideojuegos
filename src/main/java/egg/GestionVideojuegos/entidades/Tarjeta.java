package egg.GestionVideojuegos.entidades;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity 
@Getter
@Setter
public class Tarjeta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Double saldo;
    
    @Column(nullable = false)
    @CreatedDate
    private LocalDate fecAlta;
    @Column(nullable = false)
    @LastModifiedDate
    private LocalDate fecBaja;
   
}
