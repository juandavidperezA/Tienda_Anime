package tc.tiendaColeccionable.tiendaanime.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "usuarios",
        uniqueConstraints = @UniqueConstraint(
                name = "u_usuarios_email",
                columnNames = "email"
        )
)

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Usuarios {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 150)
        private String nombre;

        @Column(nullable = false, length = 50)
        private String numeroIdentificacion;

        @Column(nullable = false)
        private LocalDate fechaNacimiento;

        @Column(nullable = false, length = 254)
        private String email;

        @Column(nullable = false, length = 254)
        private String passwordHash;

        @Column(nullable = false)
        private Rol rol;

    public Usuarios(
            String nombre, String numeroIdentificacion, LocalDate fechaNacimiento, String email, String passwordHash) {

            this.nombre = nombre;
            this.numeroIdentificacion = numeroIdentificacion;
            this.fechaNacimiento = fechaNacimiento;
            this.email = email;
            this.passwordHash = passwordHash;
            this.rol = Rol.CLIENTE;
    }
}
