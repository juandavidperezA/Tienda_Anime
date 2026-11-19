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
    private Categoria categoria;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer cantidad;

}
