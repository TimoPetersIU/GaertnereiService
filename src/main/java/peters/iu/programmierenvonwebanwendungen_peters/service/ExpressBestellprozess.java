package peters.iu.programmierenvonwebanwendungen_peters.service;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;

import java.math.BigDecimal;

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
        BigDecimal expressZuschlag = bestellung.getPreis().multiply(new BigDecimal("0.10")); // 10% Expresszuschlag
        BigDecimal gesamtpreis = bestellung.getPreis().add(expressZuschlag);
        bestellung.setPreis(gesamtpreis);
        System.out.println("Zahlung für Expressbestellung #" + bestellung.getBestellnummer() + " in Höhe von " + gesamtpreis + " € durchgeführt (inkl. Expresszuschlag).");
    }

    // Zusätzlicher Schritt für Expressbestellungen
    protected void prioritaetspruefungDurchfuehren(Bestellung bestellung) {
        System.out.println("Prioritätsprüfung für Expressbestellung #" + bestellung.getBestellnummer() + " durchgeführt.");
    }
}