package peters.iu.programmierenvonwebanwendungen_peters.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Diese Entität repräsentiert einen Stammkunden.
 * Die Klasse erweitert die Basisklasse {@link Kunde} und verwendet das
 * Vererbungsmuster der Tabellenvererbung, um verschiedene Kundentypen zu unterscheiden.
 *
 * @author Timo Peters - IU Hamburg
 */
@Entity
@DiscriminatorValue("Stammkunde")
public class Stammkunde extends Kunde {

    /**
     * Konstruktor für die Erstellung eines Neukunden mit dem angegebenen Kundentyp.
     *
     * @param kundentyp der Kundentyp, der diesem Neukunden zugewiesen wird
     */
    public Stammkunde(Kundentyp kundentyp) {
        super(kundentyp);
    }

    /**
     * Standardkonstruktor für die JPA-Entität.
     * Wird von JPA benötigt, um Instanzen der Entität zu erstellen.
     */
    public Stammkunde() {

    }
}
