package peters.iu.programmierenvonwebanwendungen_peters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellposition;
import peters.iu.programmierenvonwebanwendungen_peters.entity.kunde.Kundentyp;
import peters.iu.programmierenvonwebanwendungen_peters.entity.produkt.Produkt;

import java.util.List;

/**
 * Repository für die {@link Bestellposition} Entität.
 * Dieses Repository bietet CRUD-Operationen für die {@link Bestellposition} Entität.
 * Es erweitert {@link JpaRepository}, um grundlegende Datenbankoperationen bereitzustellen.
 *
 * @author Timo Peters - IU Hamburg
 */
@Repository
public interface BestellpositionRepository extends JpaRepository<Bestellposition, Long> {

    /**
     * Findet alle Bestellpositionen, die zu einem bestimmten {@link Produkt} zugehörig sind.
     *
     * @param produkt das Produkt, nach dem gefiltert werden soll
     * @return eine Liste von Bestellpositionen, die dem angegebenen Produkt zugehörig sind
     */
    List<Bestellposition> findByProdukt(Produkt produkt);
}
