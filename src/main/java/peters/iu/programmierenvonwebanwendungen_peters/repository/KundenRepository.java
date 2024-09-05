package peters.iu.programmierenvonwebanwendungen_peters.repository;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Kundentyp;

import java.util.List;

/**
 * Repository für die {@link Kunde} Entität.
 *
 * Dieses Repository bietet CRUD-Operationen für die {@link Kunde} Entität und ermöglicht
 * die Durchführung von Abfragen über die Kunden-Tabelle in der Datenbank. Es erweitert
 * {@link JpaRepository}, um grundlegende Datenbankoperationen wie Erstellen, Lesen,
 * Aktualisieren und Löschen von Kunden zu ermöglichen.
 *
 *
 * @author Timo Peters - IU Hamburg
 */
@Repository
public interface KundenRepository extends JpaRepository<Kunde, Long> {

    /**
     * Findet alle Kunden, die zu einem bestimmten {@link Kundentyp} gehören.
     *
     * @param kundentyp der Kundentyp, nach dem gefiltert werden soll
     * @return eine Liste von Kunden, die dem angegebenen Kundentyp entsprechen
     */
    List<Kunde> findByKundentyp(Kundentyp kundentyp);
}
