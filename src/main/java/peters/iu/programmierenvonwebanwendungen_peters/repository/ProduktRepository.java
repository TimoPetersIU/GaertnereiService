package peters.iu.programmierenvonwebanwendungen_peters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peters.iu.programmierenvonwebanwendungen_peters.entity.produkt.Produkt;

/**
 * Repository für die {@link Produkt} Entität.
 * Dieses Repository bietet CRUD-Operationen für die {@link Produkt} Entität und ermöglicht
 * die Durchführung von Abfragen über die Produkt-Tabelle in der Datenbank. Es erweitert
 * {@link JpaRepository}, um grundlegende Datenbankoperationen wie Erstellen, Lesen,
 * Aktualisieren und Löschen von Produkten zu ermöglichen.
 *
 * @author Timo Peters - IU Hamburg
 */
@Repository
public interface ProduktRepository extends JpaRepository<Produkt, Long> {
}
