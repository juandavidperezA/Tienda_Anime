package tc.tiendaColeccionable.tiendaanime.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_pedido")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "productos_id", nullable = false)
    private Productos productos;

    @JoinColumn(name = "pedidos_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Pedidos pedido;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal precioUnitario;

    public DetallePedido(Productos productos, Pedidos pedido, BigDecimal precioUnitario) {
        if(pedido==null || productos == null){
            throw new IllegalArgumentException("El pedido y producto son obligatorios");
        }
        if(productos.getCantidad()<=0){
            throw new IllegalArgumentException("la cantidad no puede ser menor o igual a cero");
        }
        this.productos = productos;
        this.pedido = pedido;
        this.precioUnitario = precioUnitario;
    }
}
