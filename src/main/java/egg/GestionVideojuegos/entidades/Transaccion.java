package egg.GestionVideojuegos.entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Transaccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private Integer tipoTransaccion;
    
    @Column(nullable = true)
    private Long dniCliente;
    
    @Column(nullable = true)
    private Integer idEmpleado;
    
    @Column(nullable = true)
    private Integer idVideojuego;
    
    @Column(nullable = false)
    private Double monto;
    
    @CreatedDate
    private LocalDateTime fechaTransaccion;
    
    @Column(nullable = true)
    private LocalDate fechaDesde;
    
    @Column(nullable = true)
    private LocalDate fechaHasta;
}
