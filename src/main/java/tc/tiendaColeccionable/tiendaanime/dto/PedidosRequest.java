package tc.tiendaColeccionable.tiendaanime.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record PedidosRequest(

        @NotNull
        @Positive
        Long clienteId,

        @NotEmpty
        @Valid
        List<DetallePedidoRequest> detalles
) {
}
