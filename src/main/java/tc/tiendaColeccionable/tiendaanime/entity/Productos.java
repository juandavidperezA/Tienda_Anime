package tc.tiendaColeccionable.tiendaanime.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "Productos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 150)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "categoria_id",nullable = false)
    private Categorias categoria;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer cantidad;

    public Productos(String nombre, Categorias categoria, BigDecimal precio, Integer cantidad) {

        if(cantidad== null || cantidad<0){
            throw new IllegalArgumentException("La cantidad no puede ser nula ni negativa");
        }
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
    }
}
