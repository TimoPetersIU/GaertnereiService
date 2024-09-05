package peters.iu.programmierenvonwebanwendungen_peters.service.bestellprozess;

import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellung;

/**
 * Konkrete Implementierung des Bestellprozesses für Standardbestellungen.
 * <p>
 * Diese Klasse definiert die Schritte für die Bearbeitung einer Standardbestellung.
 * Die Schritte beinhalten die Auswahl des Produkts, die Angabe der Versandadresse
 * und die Durchführung der Zahlung. Die konkrete Implementierung dieser Schritte
 * erfolgt hier.
 * </p>
 *
 * @author Timo Peters - IU Hamburg
 */
public class StandardBestellprozess extends Bestellprozess {

    @Override
    protected void produktAuswaehlen(Bestellung bestellung) {
        System.out.println("Produkt für Bestellung #" + bestellung.getBestellnummer() + " ausgewählt.");
    }

    @Override
    protected void versandadresseAngeben(Bestellung bestellung) {
        System.out.println("Versandadresse für Bestellung #" + bestellung.getBestellnummer() + " angegeben.");
    }

    @Override
    protected void zahlungDurchfuehren(Bestellung bestellung) {
        System.out.println("Zahlung für Bestellung #" + bestellung.getBestellnummer() + " durchgeführt.");
    }
}
