package tc.tiendaColeccionable.tiendaanime.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetallePedidoRequest(
        @NotNull
        @Positive
        Long productoId,

        @NotNull
        @Positive
        Integer cantidad
) {
}
