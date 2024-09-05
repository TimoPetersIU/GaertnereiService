package peters.iu.programmierenvonwebanwendungen_peters.service;

import org.springframework.stereotype.Component;
import peters.iu.programmierenvonwebanwendungen_peters.entity.*;

/**
 * Factory-Klasse zur Erstellung von Kundenobjekten basierend auf dem Kundentyp.
 * <p>
 * Diese Klasse ist verantwortlich für die Erstellung von Kundenobjekten, die auf
 * dem übergebenen `Kundentyp` basieren. Der `Kundentyp` bestimmt die Art des
 * Kundenobjekts, das erstellt werden soll.
 * </p>
 *
 * @author Timo Peters - IU Hamburg
 */
@Component
public class KundenFactory {

    /**
     * Erstellt ein Kundenobjekt basierend auf dem übergebenen Kundentyp.
     *
     * @param kundentyp Der Typ des zu erstellenden Kunden (Privatkunde, Geschäftskunde, etc.).
     * @return Ein neues Kundenobjekt, das dem angegebenen Kundentyp entspricht.
     * @throws IllegalArgumentException Falls der angegebene Kundentyp ungültig ist.
     */
    public Kunde erstelleKunde(Kundentyp kundentyp) {
        int kundentypId = kundentyp.getTyp();

        // Bestimmt den zu erstellenden Kundentyp anhand der Kundentyp-ID
        switch (kundentypId) {
            case 1:
                return new Privatkunde(kundentyp); // Erstellt ein Privatkunde-Objekt
            case 2:
                return new Geschaeftskunde(kundentyp); // Erstellt ein Geschäftskunde-Objekt
            case 3:
                return new Grosskunde(kundentyp); // Erstellt ein Großkunde-Objekt
            case 4:
                return new Neukunde(kundentyp); // Erstellt ein Neukunde-Objekt
            case 5:
                return new Stammkunde(kundentyp); // Erstellt ein Stammkunde-Objekt
            default:
                // Wirft eine Ausnahme, wenn der angegebene Kundentyp nicht unterstützt wird
                throw new IllegalArgumentException("Ungültiger Kundentyp: " + kundentypId);
        }
    }
}
