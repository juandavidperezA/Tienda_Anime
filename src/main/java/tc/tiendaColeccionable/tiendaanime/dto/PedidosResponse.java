package tc.tiendaColeccionable.tiendaanime.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidosResponse(
        Long id,
        Long clienteId,
        LocalDateTime fecha,
        BigDecimal subtotal,
        BigDecimal descuento,
        BigDecimal total,
        String estado
) {
}
