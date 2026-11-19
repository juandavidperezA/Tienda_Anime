
package tc.tiendaColeccionable.tiendaanime.dto;

import jakarta.validation.constraints.*;

public record CategoriasRequest(
        @NotBlank
        @Size(max = 100)
        String nombre
) {
}
