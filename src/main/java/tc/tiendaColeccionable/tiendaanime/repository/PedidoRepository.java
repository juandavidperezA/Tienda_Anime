package tc.tiendaColeccionable.tiendaanime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tc.tiendaColeccionable.tiendaanime.entity.Pedidos;

@Repository
public interface PedidoRepository extends JpaRepository<Pedidos, Long> {
}
