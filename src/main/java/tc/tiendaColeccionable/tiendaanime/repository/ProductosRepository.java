package tc.tiendaColeccionable.tiendaanime.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tc.tiendaColeccionable.tiendaanime.entity.Productos;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductosRepository extends JpaRepository<Productos, Long> {

    @EntityGraph(attributePaths = "categoria")
    List<Productos> findByCategoria_Id(Long categoriaId);

    @EntityGraph(attributePaths = "categoria")
    List<Productos> findByNombre(String nombreProducto);

    @Override
    @EntityGraph(attributePaths = "categoria")
    List<Productos> findAll();

    @Override
    @EntityGraph(attributePaths = "categoria")
    Optional<Productos> findById(Long productoId);
}

