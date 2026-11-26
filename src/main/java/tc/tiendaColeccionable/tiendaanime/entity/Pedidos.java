package tc.tiendaColeccionable.tiendaanime.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "pedidos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "usuario_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Usuarios usuario;

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

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles= new ArrayList<>();

    public Pedidos(Usuarios usuarios) {
        if(usuarios==null){
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
        this.usuario = usuarios;
        this.subtotal = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.fecha = LocalDateTime.now();
        this.estado= "Pendiente";
    }

    public List<DetallePedido> getDetalles(){
        return Collections.unmodifiableList(detalles);
    }

    public void agregarDetalle(Productos productos, int cantidad){
        DetallePedido detallesP= new DetallePedido(productos,this,cantidad);

        detalles.add(detallesP);
    }

    public void calcularTotales(BigDecimal tasaDescuento){
        if(detalles.isEmpty()){
            throw new IllegalStateException("El pedido debe tener al menos un detalle");
        }

        if(tasaDescuento==null || tasaDescuento.signum()<0 ||tasaDescuento.compareTo(BigDecimal.ONE)>0){
            throw new IllegalArgumentException("La tasa de descuento debe estar entre 0 y 1");
        }

        subtotal= calcularSubtotal();

        descuento= subtotal.multiply(tasaDescuento)
                .setScale(2,RoundingMode.HALF_UP);

        total=subtotal.subtract(descuento);
    }

    public BigDecimal calcularSubtotal(){
        return detalles.stream()
                .map(DetallePedido::calcularImporte)
                .reduce(new BigDecimal("0.00"), BigDecimal::add)
                .setScale(2, RoundingMode.UNNECESSARY);

    }
}
