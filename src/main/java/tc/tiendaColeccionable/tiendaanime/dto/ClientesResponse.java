package tc.tiendaColeccionable.tiendaanime.dto;

import java.time.LocalDate;

public record ClientesResponse(
        Long id,
        String nombre,
        String correo,
        String telefono,
        String direccion,
        String ciudad,
        String departamento,
        String codigoPostal,
        LocalDate fechaRegistro
) {
}