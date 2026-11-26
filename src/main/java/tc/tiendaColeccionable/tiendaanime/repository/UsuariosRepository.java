package tc.tiendaColeccionable.tiendaanime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tc.tiendaColeccionable.tiendaanime.entity.Usuarios;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
}
