package peters.iu.programmierenvonwebanwendungen_peters.entity.kunde;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Diese Entität repräsentiert einen Großkunden.
 * Die Klasse erweitert die Basisklasse {@link Kunde} und verwendet das
 * Vererbungsmuster der Tabellenvererbung, um verschiedene Kundentypen zu unterscheiden.
 *
 * @author Timo Peters - IU Hamburg
 */
@Entity
@DiscriminatorValue("Grosskunde")
public class Grosskunde extends Kunde {

    /**
     * Konstruktor für einen Großkunden, der einen {@link Kundentyp} benötigt.
     * Dieser Konstruktor wird verwendet, um einen neuen Großkunden mit
     * einem spezifischen Kundentyp zu erstellen.
     *
     * @param kundentyp der Kundentyp des Großkunden
     */
    public Grosskunde(Kundentyp kundentyp) {
        super(kundentyp);
    }

    /**
     * Standardkonstruktor für die JPA-Entität.
     * Wird von JPA benötigt, um Instanzen der Entität zu erstellen.
     */
    public Grosskunde() {}
}
