package peters.iu.programmierenvonwebanwendungen_peters.entity.kunde;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Diese Entität repräsentiert einen Geschäftskunden.
 * Die Klasse erweitert die Basisklasse {@link Kunde} und verwendet das
 * Vererbungsmuster der Tabellenvererbung, um verschiedene Kundentypen zu unterscheiden.
 *
 * @author Timo Peters - IU Hamburg
 */
@Entity
@DiscriminatorValue("Geschaeftskunde")
public class Geschaeftskunde extends Kunde {

    /**
     * Konstruktor für einen Geschäftskunden, der einen {@link Kundentyp} benötigt.
     * Dieser Konstruktor wird verwendet, um einen neuen Geschäftskunden mit
     * einem spezifischen Kundentyp zu erstellen.
     *
     * @param kundentyp der Kundentyp des Geschäftskunden
     */
    public Geschaeftskunde(Kundentyp kundentyp) {
        super(kundentyp);
    }

    /**
     * Standardkonstruktor für die JPA-Entität.
     * Wird von JPA benötigt, um Instanzen der Entität zu erstellen.
     */
    public Geschaeftskunde() {
    }
}
