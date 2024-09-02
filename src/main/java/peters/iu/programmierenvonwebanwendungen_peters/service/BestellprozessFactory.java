package peters.iu.programmierenvonwebanwendungen_peters.service;

import org.springframework.stereotype.Component;

@Component
public class BestellprozessFactory {

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