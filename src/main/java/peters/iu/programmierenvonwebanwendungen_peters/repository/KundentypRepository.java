package peters.iu.programmierenvonwebanwendungen_peters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Kundentyp;

/**
 * Repository für die {@link Kundentyp} Entität.
 * Dieses Repository bietet CRUD-Operationen für die {@link Kundentyp} Entität und ermöglicht
 * die Durchführung von Abfragen über die Kundentyp-Tabelle in der Datenbank. Es erweitert
 * {@link JpaRepository}, um grundlegende Datenbankoperationen wie Erstellen, Lesen,
 * Aktualisieren und Löschen von Kundentypen zu ermöglichen.
 *
 * @author Timo Peters - IU Hamburg
 */
@Repository
public interface KundentypRepository extends JpaRepository<Kundentyp, Long> {
}
