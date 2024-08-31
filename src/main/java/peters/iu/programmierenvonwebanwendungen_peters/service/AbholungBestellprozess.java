package peters.iu.programmierenvonwebanwendungen_peters.service;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;

public class AbholungBestellprozess extends Bestellprozess {

    @Override
    protected void produktAuswaehlen(Bestellung bestellung) {
        System.out.println("Produkt für Abholbestellung #" + bestellung.getBestellnummer() + " ausgewählt.");
    }

    // Überschreiben der Versandadresse, da keine benötigt wird
    @Override
    protected void versandadresseAngeben(Bestellung bestellung) {
        System.out.println("Keine Versandadresse erforderlich für Abholbestellung #" + bestellung.getBestellnummer() + ".");
    }

    @Override
    protected void zahlungDurchfuehren(Bestellung bestellung) {
        System.out.println("Zahlung für Abholbestellung #" + bestellung.getBestellnummer() + " durchgeführt.");
    }

    // Zusätzlicher Schritt für Abholbestellungen
    protected void abholortAuswaehlen(Bestellung bestellung) {
        System.out.println("Abholort für Bestellung #" + bestellung.getBestellnummer() + " ausgewählt.");
    }
}