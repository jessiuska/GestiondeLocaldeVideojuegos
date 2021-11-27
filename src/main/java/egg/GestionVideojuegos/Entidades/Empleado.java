package egg.GestionVideojuegos.Entidades;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.EntityListeners;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Empleado {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String rol;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate fecAlta;

    @LastModifiedDate
    private LocalDate fecBaja;
}
