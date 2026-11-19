package tc.tiendaColeccionable.tiendaanime.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "clientes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150, unique = true)
    private String correo;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 100)
    private String departamento;

    @Column(nullable = false, length = 20)
    private String codigoPostal;

    @Column(nullable = false)
    private LocalDate fechaRegistro;

    public Clientes(String nombre, String correo, String telefono,
                    String direccion, String ciudad, String departamento,
                    String codigoPostal) {

        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.departamento = departamento;
        this.codigoPostal = codigoPostal;
        this.fechaRegistro = LocalDate.now();
    }
}