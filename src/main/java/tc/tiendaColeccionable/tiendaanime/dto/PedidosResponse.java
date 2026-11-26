package tc.tiendaColeccionable.tiendaanime.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidosResponse(
        Long id,
        Long usuarioId,
        LocalDateTime fecha,
        BigDecimal subtotal,
        BigDecimal descuento,
        BigDecimal total,
        String estado,
        List<DetallePedidoResponse> detalles
) {
}
