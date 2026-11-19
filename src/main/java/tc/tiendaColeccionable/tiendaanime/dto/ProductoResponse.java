package tc.tiendaColeccionable.tiendaanime.dto;

import java.math.BigDecimal;

public record ProductoResponse(
        Long id,
        String nombre,
        Long categoriaId,
        BigDecimal precio,
        Integer cantidad
) {
}
