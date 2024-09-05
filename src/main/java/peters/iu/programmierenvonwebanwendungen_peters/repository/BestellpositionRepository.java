package peters.iu.programmierenvonwebanwendungen_peters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellposition;

/**
 * Repository für die {@link Bestellposition} Entität.
 * Dieses Repository bietet CRUD-Operationen für die {@link Bestellposition} Entität.
 * Es erweitert {@link JpaRepository}, um grundlegende Datenbankoperationen bereitzustellen.
 *
 * @author Timo Peters - IU Hamburg
 */
@Repository
public interface BestellpositionRepository extends JpaRepository<Bestellposition, Long> {
}
