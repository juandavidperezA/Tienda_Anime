package tc.tiendaColeccionable.tiendaanime.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tc.tiendaColeccionable.tiendaanime.entity.Pedidos;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedidos, Long> {

    @Override
    @EntityGraph(attributePaths = {"detalles","detalles.productos"})
    Optional<Pedidos> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"detalles","detalles.productos"})
    List<Pedidos> findAll();

    @EntityGraph(attributePaths = {
            "detalles",
            "detalles.producto"
    })
    Optional<Pedidos> findByIdAndUsuario_Id(
            Long id,
            Long usuarioId
    );

    @EntityGraph(attributePaths = {
            "detalles",
            "detalles.producto"
    })
    List<Pedidos> findByUsuario_Id(Long usuarioId);
}
