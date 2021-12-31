package egg.GestionVideojuegos.entidades;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import egg.GestionVideojuegos.modelos.ClienteModel;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE cliente SET alta = false WHERE dni = ?")
@NamedNativeQuery(
    name = "buscar_top5_clientes",
    query = "SELECT c.nombre AS nombre, c.apellido AS apellido, COUNT(t.dni_cliente) AS jugados " +
            "FROM transaccion t " +
            "INNER JOIN cliente c ON c.dni = t.dni_cliente " +
            "WHERE t.tipo_transaccion = 2 " +
            "GROUP BY c.nombre, c.apellido " +
            "ORDER BY 3 DESC, c.apellido ASC LIMIT 5",
    resultSetMapping = "cliente_dto"
)
@SqlResultSetMapping(
    name = "cliente_dto",
    classes = @ConstructorResult(
        targetClass = ClienteModel.class,
        columns = {
            @ColumnResult(name = "nombre", type = String.class),
            @ColumnResult(name = "apellido", type = String.class),
            @ColumnResult(name = "jugados", type = Integer.class)
        }
    )
)
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
    /*
    @Enumerated(EnumType.STRING)
    //@Column(nullable = false)
    private Rol rol;
    */
    private Boolean alta;
    
}
