package tc.tiendaColeccionable.tiendaanime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tc.tiendaColeccionable.tiendaanime.entity.Clientes;

@Repository
public interface ClienteRepository extends JpaRepository<Clientes, Long> {
}
