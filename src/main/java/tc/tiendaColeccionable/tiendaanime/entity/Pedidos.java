package tc.tiendaColeccionable.tiendaanime.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "pedidos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "cliente_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Clientes clientes;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal descuento;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal total;

    @Column(nullable = false,length = 150)
    private String estado;

    public Pedidos(Clientes clientes, BigDecimal total, BigDecimal subtotal, BigDecimal descuento, String estado) {
        this.clientes = clientes;
        this.total = total;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.estado = estado;
    }
}
