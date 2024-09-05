package peters.iu.programmierenvonwebanwendungen_peters.entity.kunde;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Diese Entität repräsentiert einen Privatkunden.
 * Die Klasse erweitert die Basisklasse {@link Kunde} und verwendet das
 * Vererbungsmuster der Tabellenvererbung, um verschiedene Kundentypen zu unterscheiden.
 *
 * @author Timo Peters - IU Hamburg
 */
@Entity
@DiscriminatorValue("Privatkunde")
public class Privatkunde extends Kunde {

    /**
     * Konstruktor für die Erstellung eines Neukunden mit dem angegebenen Kundentyp.
     *
     * @param kundentyp der Kundentyp, der diesem Neukunden zugewiesen wird
     */
    public Privatkunde(Kundentyp kundentyp) {
        super(kundentyp);
    }

    /**
     * Standardkonstruktor für die JPA-Entität.
     * Wird von JPA benötigt, um Instanzen der Entität zu erstellen.
     */
    public Privatkunde() {
    }
}
