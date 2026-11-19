package tc.tiendaColeccionable.tiendaanime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tc.tiendaColeccionable.tiendaanime.entity.Productos;

@Repository
public interface ProductosRepository extends JpaRepository<Productos, Long> {
}
