package peters.iu.programmierenvonwebanwendungen_peters.service;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;

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