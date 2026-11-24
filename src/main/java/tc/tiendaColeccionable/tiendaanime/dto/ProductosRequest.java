package tc.tiendaColeccionable.tiendaanime.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;


public record ProductosRequest(
        @NotBlank
        @Size(max = 150)
        String nombre,

        @NotNull
        @Digits(integer = 17, fraction = 2)
        @DecimalMin("0.01")
        BigDecimal precio,

        @PositiveOrZero
        @NotNull
        Integer cantidad,

        @NotNull
        @Positive
        Long categoriaId
) {
}
