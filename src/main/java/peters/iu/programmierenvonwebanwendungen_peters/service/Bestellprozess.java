package peters.iu.programmierenvonwebanwendungen_peters.service;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;

/**
 * Abstrakte Basisklasse für den Bestellprozess.
 * <p>
 * Diese Klasse definiert den allgemeinen Ablauf eines Bestellprozesses durch die
 * Methode {@code bestellungVerarbeiten}, die die Schritte zur Bearbeitung einer Bestellung
 * in einer festgelegten Reihenfolge ausführt. Die spezifischen Schritte wie Produktauswahl,
 * Versandadresse angeben und Zahlung durchführen werden durch die abgeleiteten Klassen
 * implementiert.
 * </p>
 *
 * @author Timo Peters - IU Hamburg
 */
public abstract class Bestellprozess {

    /**
     * Verarbeitet die Bestellung durch Ausführen der einzelnen Schritte des Bestellprozesses.
     * Die Schritte werden in der folgenden Reihenfolge ausgeführt:
     * <ol>
     *     <li>Produkt auswählen</li>
     *     <li>Versandadresse angeben</li>
     *     <li>Zahlung durchführen</li>
     *     <li>Bestellbestätigung senden</li>
     * </ol>
     *
     * @param bestellung die Bestellung, die bearbeitet werden soll
     */
    public final void bestellungVerarbeiten(Bestellung bestellung) {
        produktAuswaehlen(bestellung);
        versandadresseAngeben(bestellung);
        zahlungDurchfuehren(bestellung);
        bestaetigungSenden(bestellung);
    }

    /**
     * Wählt das Produkt für die Bestellung aus.
     *
     * @param bestellung die Bestellung, für die das Produkt ausgewählt werden soll
     */
    protected abstract void produktAuswaehlen(Bestellung bestellung);

    /**
     * Gibt die Versandadresse für die Bestellung an.
     *
     * @param bestellung die Bestellung, für die die Versandadresse angegeben werden soll
     */
    protected abstract void versandadresseAngeben(Bestellung bestellung);

    /**
     * Führt die Zahlung für die Bestellung durch.
     *
     * @param bestellung die Bestellung, für die die Zahlung durchgeführt wird
     */
    protected abstract void zahlungDurchfuehren(Bestellung bestellung);

    /**
     * Sendet eine Bestellbestätigung für die Bestellung.
     * Dieser Schritt ist im Basisklassen-Bestellprozess definiert und wird für jede Bestellung
     * durchgeführt. Die Bestätigung wird über die Konsole ausgegeben.
     *
     * @param bestellung die Bestellung, für die die Bestellbestätigung gesendet wird
     */
    private void bestaetigungSenden(Bestellung bestellung) {
        System.out.println("Bestellbestätigung für Bestellung #" + bestellung.getBestellnummer() + " wurde gesendet.");
    }
}
