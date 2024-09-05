package peters.iu.programmierenvonwebanwendungen_peters.service;

import org.springframework.stereotype.Component;

/**
 * Factory-Klasse zur Erstellung von {@link Bestellprozess} Instanzen.
 * <p>
 * Diese Klasse ist verantwortlich für die Erstellung von konkreten Implementierungen des
 * {@link Bestellprozess}-Abstraktionss. Die Methode {@code getBestellprozess} gibt je nach
 * angegebenem Typ die entsprechende Bestellprozess-Instanz zurück.
 * </p>
 *
 * @author Timo Peters - IU Hamburg
 */
@Component
public class BestellprozessFactory {

    /**
     * Gibt eine Instanz des {@link Bestellprozess} zurück, basierend auf dem angegebenen Typ.
     * <p>
     * Die Methode überprüft den angegebenen Typ und erstellt die entsprechende Implementierung
     * des Bestellprozesses:
     * <ul>
     *     <li>"standard" - Gibt eine Instanz von {@link StandardBestellprozess} zurück.</li>
     *     <li>"express" - Gibt eine Instanz von {@link ExpressBestellprozess} zurück.</li>
     *     <li>"abholung" - Gibt eine Instanz von {@link AbholungBestellprozess} zurück.</li>
     * </ul>
     * Falls der Typ nicht übereinstimmt, wird eine {@link IllegalArgumentException} geworfen.
     * </p>
     *
     * @param typ der Typ des Bestellprozesses, der erstellt werden soll
     * @return eine Instanz der konkreten Implementierung des Bestellprozesses
     * @throws IllegalArgumentException wenn der angegebene Typ ungültig ist
     */
    public Bestellprozess getBestellprozess(String typ) {
        if (typ.equalsIgnoreCase("standard")) {
            return new StandardBestellprozess();
        } else if (typ.equalsIgnoreCase("express")) {
            return new ExpressBestellprozess();
        } else if (typ.equalsIgnoreCase("abholung")) {
            return new AbholungBestellprozess();
        } else {
            throw new IllegalArgumentException("Ungültiger Bestellprozess-Typ: " + typ);
        }
    }
}
