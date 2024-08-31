package peters.iu.programmierenvonwebanwendungen_peters.service;

import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;

public abstract class Bestellprozess {

    // Template-Methode, die den allgemeinen Ablauf des Bestellprozesses definiert
    public final void bestellungVerarbeiten(Bestellung bestellung) {
        produktAuswaehlen(bestellung);
        versandadresseAngeben(bestellung);
        zahlungDurchfuehren(bestellung);
        bestaetigungSenden(bestellung);
    }

    // Abstrakte Methoden, die von Unterklassen implementiert werden müssen
    protected abstract void produktAuswaehlen(Bestellung bestellung);
    protected abstract void versandadresseAngeben(Bestellung bestellung);
    protected abstract void zahlungDurchfuehren(Bestellung bestellung);

    // Konkrete Methode, die in der Basisklasse implementiert ist
    private void bestaetigungSenden(Bestellung bestellung) {
        System.out.println("Bestellbestätigung für Bestellung #" + bestellung.getBestellnummer() + " wurde gesendet.");
    }
}