package egg.GestionVideojuegos.modelos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteModel {
    private String nombre;
    private String apellido;
    private Integer jugados;
}