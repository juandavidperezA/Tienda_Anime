package tc.tiendaColeccionable.tiendaanime.dto;

import java.math.BigDecimal;

public record DetallePedidoResponse(
        Long id,
        Long productosId,
        Long pedidosId,
        BigDecimal precioUnitario
) {
}
