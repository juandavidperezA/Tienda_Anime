package tc.tiendaColeccionable.tiendaanime.dto;

import jakarta.validation.constraints.*;

public record ClientesRequest(

        @NotBlank
        @Size(max = 150)
        String nombre,

        @NotBlank
        @Email
        @Size(max = 150)
        String correo,

        @NotBlank
        @Size(max = 20)
        String telefono,

        @NotBlank
        @Size(max = 200)
        String direccion,

        @NotBlank
        @Size(max = 100)
        String ciudad,

        @NotBlank
        @Size(max = 100)
        String departamento,

        @NotBlank
        @Size(max = 20)
        String codigoPostal

) {
}