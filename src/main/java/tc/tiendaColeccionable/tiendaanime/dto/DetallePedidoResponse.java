package tc.tiendaColeccionable.tiendaanime.dto;

import java.math.BigDecimal;

public record DetallePedidoResponse(
        Long id,
        String productosNombre,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal importe
) {
}
