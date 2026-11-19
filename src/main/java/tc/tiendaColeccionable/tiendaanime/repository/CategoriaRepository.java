
package tc.tiendaColeccionable.tiendaanime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tc.tiendaColeccionable.tiendaanime.entity.Categorias;

@Repository
public interface CategoriaRepository extends JpaRepository<Categorias, Long> {
}
