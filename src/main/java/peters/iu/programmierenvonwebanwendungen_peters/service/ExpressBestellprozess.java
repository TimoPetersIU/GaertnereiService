package peters.iu.programmierenvonwebanwendungen_peters.service;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;

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
        // Berechnet den Expresszuschlag als 10% des Bestellpreises
        BigDecimal expressZuschlag = bestellung.getPreis().multiply(new BigDecimal("0.10")); // 10% Expresszuschlag

        // Berechnet den neuen Gesamtpreis einschließlich des Expresszuschlags
        BigDecimal gesamtpreis = bestellung.getPreis().add(expressZuschlag);

        // Setzt den neuen Preis in der Bestellung
        bestellung.setPreis(gesamtpreis);

        // Gibt eine Nachricht über den Zahlungsvorgang aus, einschließlich des Expresszuschlags
        System.out.println("Zahlung für Expressbestellung #" + bestellung.getBestellnummer() + " in Höhe von " + gesamtpreis + " € durchgeführt (inkl. Expresszuschlag).");
    }

    // Zusätzlicher Schritt für Expressbestellungen
    protected void prioritaetspruefungDurchfuehren(Bestellung bestellung) {
        System.out.println("Prioritätsprüfung für Expressbestellung #" + bestellung.getBestellnummer() + " durchgeführt.");
    }
}
