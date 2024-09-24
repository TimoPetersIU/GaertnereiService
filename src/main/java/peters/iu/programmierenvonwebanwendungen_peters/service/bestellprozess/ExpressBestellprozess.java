package peters.iu.programmierenvonwebanwendungen_peters.service.bestellprozess;

import peters.iu.programmierenvonwebanwendungen_peters.entity.bestellung.Bestellung;

import java.math.BigDecimal;

/**
 * Repräsentiert den Bestellprozess für Expressbestellungen.
 * <p>
 * Diese Klasse implementiert die spezifischen Schritte für den Expressbestellprozess.
 * Dazu gehört die Auswahl des Produkts, die Angabe der Versandadresse, die Durchführung
 * der Zahlung (einschließlich eines Expresszuschlags) und die Prioritätsprüfung.
 * </p>
 *
 * @author Timo Peters - IU Hamburg
 */
public class ExpressBestellprozess extends Bestellprozess {

    private static final BigDecimal EXPRESS_ZUSCHLAG = new BigDecimal("5.00"); // Fester Expresszuschlag

    @Override
    protected void produktAuswaehlen(Bestellung bestellung) {
        System.out.println("Produkt für Expressbestellung #" + bestellung.getBestellnummer() + " ausgewählt.");
    }

    @Override
    protected void versandadresseAngeben(Bestellung bestellung) {
        System.out.println("Versandadresse für Expressbestellung #" + bestellung.getBestellnummer() + " angegeben.");
    }

    @Override
    protected void zahlungDurchfuehren(Bestellung bestellung) {
        // Berechnet den neuen Gesamtpreis einschließlich des Expresszuschlags
        BigDecimal gesamtpreis = bestellung.getPreis().add(EXPRESS_ZUSCHLAG);

        // Setzt den neuen Preis in der Bestellung
        bestellung.setPreis(gesamtpreis);

        System.out.println("Zahlung für Expressbestellung #" + bestellung.getBestellnummer() + " in Höhe von " + gesamtpreis + " € durchgeführt (inkl. Expresszuschlag).");
    }

    // Zusätzlicher Schritt für Expressbestellungen
    protected void prioritaetspruefungDurchfuehren(Bestellung bestellung) {
        System.out.println("Prioritätsprüfung für Expressbestellung #" + bestellung.getBestellnummer() + " durchgeführt.");
    }
}