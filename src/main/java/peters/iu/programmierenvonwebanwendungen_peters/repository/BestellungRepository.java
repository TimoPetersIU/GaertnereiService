package peters.iu.programmierenvonwebanwendungen_peters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellung;

/**
 * Repository für die {@link Bestellung} Entität.
 * Dieses Repository bietet CRUD-Operationen für die {@link Bestellung} Entität.
 * Es erweitert {@link JpaRepository}, um grundlegende Datenbankoperationen wie Erstellen,
 * Lesen, Aktualisieren und Löschen von Bestellungen zu ermöglichen.
 *
 * @author Timo Peters - IU Hamburg
 */
@Repository
public interface BestellungRepository extends JpaRepository<Bestellung, Long> {
}
