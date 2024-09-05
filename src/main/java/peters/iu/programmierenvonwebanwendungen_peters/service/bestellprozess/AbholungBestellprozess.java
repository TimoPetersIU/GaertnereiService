package peters.iu.programmierenvonwebanwendungen_peters.service.bestellprozess;

import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellung;

/**
 * Implementierung des Bestellprozesses für Abholbestellungen.
 * Diese Klasse implementiert den speziellen Bestellprozess für Abholbestellungen.
 * Der Abholprozess unterscheidet sich von anderen Bestellprozessen, indem keine
 * Versandadresse benötigt wird und ein zusätzlicher Schritt zur Auswahl des Abholortes
 * hinzugefügt wird. Die Methoden in dieser Klasse überschreiben die entsprechenden
 * Schritte des Basisklassen-Bestellprozesses.
 *
 * @author Timo Peters - IU Hamburg
 */
public class AbholungBestellprozess extends Bestellprozess {

    @Override
    protected void produktAuswaehlen(Bestellung bestellung) {
        System.out.println("Produkt für Abholbestellung #" + bestellung.getBestellnummer() + " ausgewählt.");
    }

    @Override
    protected void versandadresseAngeben(Bestellung bestellung) {
        System.out.println("Keine Versandadresse erforderlich für Abholbestellung #" + bestellung.getBestellnummer() + ".");
    }

    @Override
    protected void zahlungDurchfuehren(Bestellung bestellung) {
        System.out.println("Zahlung für Abholbestellung #" + bestellung.getBestellnummer() + " durchgeführt.");
    }

    /**
     * Wählt den Abholort für die Abholbestellung aus.
     *
     * @param bestellung die Bestellung, für die der Abholort ausgewählt wird
     */
    protected void abholortAuswaehlen(Bestellung bestellung) {
        System.out.println("Abholort für Bestellung #" + bestellung.getBestellnummer() + " ausgewählt.");
    }
}
