package peters.iu.programmierenvonwebanwendungen_peters.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellposition;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Bestellung;
import peters.iu.programmierenvonwebanwendungen_peters.entity.Produkt;
import peters.iu.programmierenvonwebanwendungen_peters.repository.ProduktRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service-Klasse zur Verwaltung von Bestellungen.
 * <p>
 * Diese Klasse enthält Methoden zur Erzeugung von Bestellpositionen und zur Berechnung des Gesamtpreises
 * einer Bestellung. Die {@code erzeugeBestellpositionen} Methode erstellt Bestellpositionen basierend
 * auf den übergebenen Produkt-IDs und Mengen. Die {@code berechneGesamtpreis} Methode berechnet den Gesamtpreis
 * der übergebenen Bestellpositionen.
 * </p>
 *
 * @author Timo Peters - IU Hamburg
 */
@Service
public class BestellungService {

    @Autowired
    private ProduktRepository produktRepository;

    /**
     * Erzeugt eine Liste von {@link Bestellposition} Objekten basierend auf den übergebenen Produkt-IDs und Mengen.
     * <p>
     * Diese Methode verwendet die Produkt-IDs, um die zugehörigen {@link Produkt} Entitäten aus dem
     * {@link ProduktRepository} abzurufen. Anschließend erstellt sie für jede Produkt-ID und Menge
     * eine {@link Bestellposition} und fügt sie der Liste der Bestellpositionen hinzu.
     * </p>
     *
     * @param produktIds die Liste von IDs der Produkte, die in die Bestellung aufgenommen werden sollen
     * @param mengen die Liste der Mengen, die für die jeweiligen Produkte bestellt werden
     * @param bestellung die Bestellung, zu der die Bestellpositionen gehören
     * @return eine Liste von {@link Bestellposition} Objekten
     * @throws IllegalArgumentException wenn eine Produkt-ID ungültig ist
     */
    public List<Bestellposition> erzeugeBestellpositionen(List<Long> produktIds, List<Integer> mengen, Bestellung bestellung) {
        // Initialisiert eine leere Liste, um die Bestellpositionen zu speichern
        List<Bestellposition> bestellpositionen = new ArrayList<>();

        // Iteriert durch die Listen der Produkt-IDs und Mengen
        for (int i = 0; i < produktIds.size(); i++) {
            // Holt die aktuelle Produkt-ID und Menge
            Long produktId = produktIds.get(i);
            int menge = mengen.get(i);

            // Sucht das Produkt in der Datenbank anhand der Produkt-ID
            Produkt produkt = produktRepository.findById(produktId)
                    .orElseThrow(() -> new IllegalArgumentException("Ungültige Produkt-ID: " + produktId));

            // Erzeugt eine neue Bestellposition und setzt die Bestellinformationen
            Bestellposition bestellposition = new Bestellposition();
            bestellposition.setBestellung(bestellung);
            bestellposition.setProdukt(produkt);
            bestellposition.setMenge(menge);

            // Fügt die Bestellposition der Liste hinzu
            bestellpositionen.add(bestellposition);
        }

        return bestellpositionen;
    }

    /**
     * Berechnet den Gesamtpreis der angegebenen {@link Bestellposition} Objekte.
     * <p>
     * Die Methode summiert die Preise aller Produkte in den Bestellpositionen, multipliziert mit der jeweiligen
     * Menge und gibt den Gesamtpreis als {@link BigDecimal} zurück.
     * </p>
     *
     * @param bestellpositionen die Liste der Bestellpositionen, deren Gesamtpreis berechnet werden soll
     * @return der Gesamtpreis als {@link BigDecimal}
     */
    public BigDecimal berechneGesamtpreis(List<Bestellposition> bestellpositionen) {
        return bestellpositionen.stream()
                .map(bp -> bp.getProdukt().getPreis().multiply(BigDecimal.valueOf(bp.getMenge()))) // Preis der Bestellposition berechnen
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Alle Preise summieren
    }
}
